package br.com.hackathon.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.hackathon.hibernate.Buscador;
import br.com.hackathon.modelo.entidade.Ocorrencia;
import br.com.hackathon.modelo.entidade.Usuario;
import br.com.hackathon.sessao.SessaoGeral;
import br.com.hackathon.util.TwitterBot;
import br.com.hackathon.util.Util;

@Resource
public class TtController {
	
	private final Result result;
	private Validator validator;
	private SessaoGeral sessaoGeral; 
	
	public TtController(Result result, Validator validator, SessaoGeral sessaoGeral){
		this.result = result;
		this.validator = validator;
		this.sessaoGeral = sessaoGeral;
	}
	
	@Path("/tt/{ocorrencia.link}")
	public void tt(Ocorrencia ocorrencia, HttpServletRequest request, HttpServletResponse response){
		if(Util.preenchido(ocorrencia.getLink())){
			Usuario usuario = TwitterBot.busyList.get(ocorrencia.getLink());
			if(Util.preenchido(usuario)){
				usuario = new Buscador().selecionar(new Usuario(usuario.getId()));
				Ocorrencia ocorrenciaReal = new Ocorrencia();
				ocorrenciaReal.setUsuario(usuario);
				ocorrenciaReal = new Buscador().ultimo(ocorrenciaReal);
				if(Util.preenchido(ocorrenciaReal)){
					sessaoGeral.setOcorrencia(ocorrenciaReal);
					sessaoGeral.setUsuario(usuario);
					result.redirectTo(HomeController.class).logarTwitter(request, response);
					return;
				}
			}			
		}
		result.redirectTo(HomeController.class).home();
	}

	
}
