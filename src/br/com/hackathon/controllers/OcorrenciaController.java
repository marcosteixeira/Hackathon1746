package br.com.hackathon.controllers;

import java.util.List;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;
import br.com.hackathon.hibernate.Buscador;
import br.com.hackathon.modelo.entidade.Assinatura;
import br.com.hackathon.modelo.entidade.Endereco;
import br.com.hackathon.modelo.entidade.Ocorrencia;
import br.com.hackathon.modelo.entidade.Servico;
import br.com.hackathon.modelo.entidade.Usuario;
import br.com.hackathon.services.BaseService;
import br.com.hackathon.services.OcorrenciaService;
import br.com.hackathon.sessao.SessaoGeral;
import br.com.hackathon.util.Util;

@Resource
public class OcorrenciaController {
	
	private final Result result;
	private Validator validator;
	private SessaoGeral sessaoGeral;
	
	public OcorrenciaController(Result result, Validator validator, SessaoGeral sessaoGeral){
		this.result = result;
		this.validator = validator;
		this.sessaoGeral = sessaoGeral;
	}
	
	public void salvar(Ocorrencia ocorrencia) {
		ocorrencia = sessaoGeral.getOcorrencia();
		new OcorrenciaService(validator).salvarOcorrencia(ocorrencia);
		result.redirectTo(this).visualizarOcorrencia(ocorrencia);
	}
	
	public void preencherSolicitante(Ocorrencia ocorrencia) {
		sessaoGeral.getOcorrencia().setServico(ocorrencia.getServico());
		sessaoGeral.getOcorrencia().setDescricao(ocorrencia.getDescricao());
		sessaoGeral.getOcorrencia().getEndereco().setLogradouro(ocorrencia.getEndereco().getLogradouro());
		sessaoGeral.getOcorrencia().getEndereco().setNumero(ocorrencia.getEndereco().getNumero());
		sessaoGeral.getOcorrencia().getEndereco().setPontoReferencia(ocorrencia.getEndereco().getPontoReferencia());
	}
	
	public Ocorrencia confirmarOcorrencia(Usuario usuario) {
		sessaoGeral.getOcorrencia().setUsuario(usuario);
		sessaoGeral.getOcorrencia().setServico(new Buscador().selecionar(sessaoGeral.getOcorrencia().getServico()));
		
		return sessaoGeral.getOcorrencia();
	}
	
	/**
	 * Método de criar uma ocorrência
	 * @param ocorrencia
	 */
	@Path("/ocorrencia/")
	public Ocorrencia index(Ocorrencia ocorrencia){
		
		if(Util.vazio(ocorrencia) || Util.vazio(ocorrencia.getEndereco()) || Util.vazio(ocorrencia.getEndereco().getCidade())){
			result.redirectTo(HomeController.class).home();
		}else{
			new OcorrenciaService(this.validator).validarCidade(ocorrencia.getEndereco().getCidade());
			result.include("servicos", new Buscador().listar2(new Servico()));
			sessaoGeral.setOcorrencia(ocorrencia);
		}

		return ocorrencia;
	}
	
	@Path("/ocorrencia/{ocorrencia.endereco.bairro}")
	public void  autocompletarOcorrenciasProximas(Ocorrencia ocorrencia){
		Ocorrencia filtro = new Ocorrencia();
		filtro.setEndereco(new Endereco());
		filtro.getEndereco().setBairro(ocorrencia.getEndereco().getBairro());
		
		List<Ocorrencia> lista = new Buscador().listar2(filtro);
		result.use(Results.json()).from(lista).include("endereco").include("servico").serialize();
		
	}
	
	@Path("/ocorrencia/visualizarOcorrencia/{ocorrencia.link}")
	public Ocorrencia visualizarOcorrencia(Ocorrencia ocorrencia){
		if(Util.vazio(ocorrencia.getEndereco())){
			ocorrencia = new Buscador().selecionar(ocorrencia);
			sessaoGeral.setOcorrencia(ocorrencia);
		}
		BaseService.inicializa(ocorrencia, ocorrencia.getAssinaturas());
		if(Util.preenchido(sessaoGeral.getOcorrencia())){
			BaseService.inicializa(sessaoGeral.getOcorrencia(), sessaoGeral.getOcorrencia().getAssinaturas());
		}
		return ocorrencia;
	}
	
	public Ocorrencia criarOcorrencia(Ocorrencia ocorrencia){
		ocorrencia = sessaoGeral.getOcorrencia();
		ocorrencia.setStatus("Em Aberto");
		result.include("listaServicos", new Buscador().listar2(new Servico()));
		return ocorrencia;
	}
	
	public void assinar(Usuario usuario){
		if(Util.preenchido(usuario.getEmail())){
		Assinatura assinatura = new Assinatura();
		Ocorrencia ocorrencia = new Ocorrencia();
		ocorrencia = new Ocorrencia();
		ocorrencia.setId(sessaoGeral.getOcorrencia().getId());
		
		assinatura.setEmail(usuario.getEmail());
		assinatura.setOcorrencia(ocorrencia);
		if(Buscador.contarElementos(assinatura) == 0){
			BaseService.salvar(assinatura);
		}
		
		result.redirectTo(this).visualizarOcorrencia(sessaoGeral.getOcorrencia());		
		}else{
			result.redirectTo(this).visualizarOcorrencia(sessaoGeral.getOcorrencia());
		}
	}
}
