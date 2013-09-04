package br.com.hackathon.services;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.hackathon.controllers.HomeController;
import br.com.hackathon.hibernate.Buscador;
import br.com.hackathon.modelo.entidade.Ocorrencia;
import br.com.hackathon.util.GeradorDeMd5;
import br.com.hackathon.util.Util;
import br.com.hackathon.util.UtilData;

public class OcorrenciaService extends BaseService {
	
	private static final String RIO_DE_JANEIRO = "Rio de Janeiro";


	public OcorrenciaService(Validator validator){
		this.validator = validator;
	}
	
	private static final Logger log = Logger.getLogger(OcorrenciaService.class);


	public void salvarOcorrencia(Ocorrencia ocorrencia) {
		ocorrencia.setDataCriacao(UtilData.agora());
		geraLinkOcorrencia(ocorrencia);
		validarOcorrencia(ocorrencia);
		salvar(ocorrencia.getUsuario());
		salvar(ocorrencia.getEndereco());
		salvar(ocorrencia);
	}

	private void geraLinkOcorrencia(Ocorrencia ocorrencia) {
		Ocorrencia filtro = new Ocorrencia();
		filtro.setLink(GeradorDeMd5.getStringAleatoria().substring(0, 6).toLowerCase());
		
		while (Buscador.contarElementos(filtro) > 0) {
			filtro.setLink(GeradorDeMd5.getStringAleatoria().substring(0, 6).toLowerCase());
		}

		ocorrencia.setLink(filtro.getLink());
	}
	

	private void validarOcorrencia(Ocorrencia ocorrencia) {
		
		log.info("Criando ocorrência");
		
		if(Util.vazio(ocorrencia)){
			validator.add(new ValidationMessage("Ocorrência inválida.", "Erro"));
		
		}else{
			
			if(Util.vazio(ocorrencia.getEndereco())){
				validator.add(new ValidationMessage("Ocorrência sem endereço.", "Erro"));
			}
			
			if(Util.vazio(ocorrencia.getLink())){
				validator.add(new ValidationMessage("Ocorrência sem link.", "Erro"));
			}
			
//			if(Util.vazio(ocorrencia.getProtocolo())){
//				validator.add(new ValidationMessage("Ocorrência sem protocolo.", "Erro"));
//			}
			
			if(Util.vazio(ocorrencia.getServico())){
				validator.add(new ValidationMessage("Ocorrência sem tipo.", "Erro"));
			}
			
//			if(Util.vazio(ocorrencia.getStatus())){
//				validator.add(new ValidationMessage("Ocorrência sem status.", "Erro"));
//			}
			
			if(Util.vazio(ocorrencia.getUsuario())){
				validator.add(new ValidationMessage("Ocorrência sem usuário.", "Erro"));
			}
		}

		validator.onErrorRedirectTo(HomeController.class).home();
	}

	public void validarCidade(String cidade) {
		
		if(!RIO_DE_JANEIRO.equals(cidade)){
			validator.add(new ValidationMessage("Infelizmente o 1746 está disponível apenas para o Rio de Janeiro.", "Erro"));
		}
		
		validator.onErrorForwardTo(HomeController.class).home();
		
	}
	
}

