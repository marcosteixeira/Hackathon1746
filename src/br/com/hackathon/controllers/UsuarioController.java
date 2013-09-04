package br.com.hackathon.controllers;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.hackathon.sessao.SessaoGeral;

@Resource
public class UsuarioController {
	
	private final Result result;
	private Validator validator;
	private SessaoGeral sessaoGeral;

	public UsuarioController(Result result, Validator validator, SessaoGeral sessaoGeral) {
		this.result = result;
		this.validator = validator;
		this.sessaoGeral = sessaoGeral;
	}
	
	public void home(){
		
	}
}
