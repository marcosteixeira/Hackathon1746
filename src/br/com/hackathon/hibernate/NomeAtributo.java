package br.com.hackathon.hibernate;

import br.com.hackathon.modelo.entidade.Entidade;
import br.com.hackathon.util.Util;

/**
 * Essa classe � usada pelo buscador para comparar o valor de 2 propriedades.
 */

public class NomeAtributo {
	
	private String nomeAtributo;
	private Entidade entidade;
	
	public NomeAtributo(String nomeAtributo){
		this.setNomeAtributo(nomeAtributo);
	}
	
	public NomeAtributo(String nomeAtributo, Entidade entidade){
		this.setNomeAtributo(nomeAtributo);
		this.setEntidade(entidade);
	}

	public void setNomeAtributo(String nomeAtributo) {
		this.nomeAtributo = nomeAtributo;
	}

	public String getNomeAtributo() {
		return nomeAtributo;
	}

	public void setEntidade(Entidade entidade) {
		this.entidade = entidade;
	}

	public Entidade getEntidade() {
		return entidade;
	}
	
	public String retornaAtributoEntidade() {
		
		if(Util.preenchido(this.entidade)) {
			return Finder.geraAlias(this.entidade) + "." + this.nomeAtributo;
		}
		
		return this.nomeAtributo;
	}
}