package br.com.hackathon.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;

import br.com.hackathon.util.Util;

/**
 * Classe que simplifica a adi��o de ordena��es nas consultas do Hibernate 
 */
public class Ordenacoes implements Iterable<Ordenacao>, Iterator<Ordenacao> {
	
	/**
	 * Array com os crit�rios de ordena��o que devem ser utilizados na consulta
	 */
	private List<Ordenacao> ordenacoes;
	
	private Integer iteracaoAtual;
	
	public Ordenacoes(Ordenacao ... ordenacoes) {
		this();
		for (Ordenacao ordenacao: ordenacoes){
			this.ordenacoes.add(ordenacao);
		}
	}
	
	public Ordenacoes(){
		this.ordenacoes = new ArrayList<Ordenacao>();
	}
	
	public Ordenacoes(Ordenacoes ... ordenacoesArray) {
		this();
		for (Ordenacoes ordenacoes: ordenacoesArray){
			for (Ordenacao ordenacao: ordenacoes){
				this.ordenacoes.add(ordenacao);
			}
		}
	}

	/**
	 * Adiciona os crit�rios de ordena��o a um objeto Criteria
	 * @param criteria
	 */
	public void gerarOrdenacao(Criteria criteria) {
		
		List<String> aliasCriados = new ArrayList<String>();
		
		if(ordenacoes != null) {
			for(Ordenacao ordenacao : ordenacoes) {
				if(ordenacao.getNomeAtributo().contains(".")) {
					ordenacao.setNomeAtributo(UtilHibernate.geraAlias(criteria, ordenacao.getNomeAtributo(), aliasCriados));
				}
				
				criteria.addOrder(ordenacao.gerarOrder());
			}
		}
	}

	public List<Ordenacao> getOrdenacoes() {
		return ordenacoes;
	}

	public boolean vazio(){
		return (this.ordenacoes == null) || (ordenacoes.size() == 0);
	}

	public boolean hasNext() {
		return Util.preenchido(this.ordenacoes) && (this.iteracaoAtual <= this.ordenacoes.size() - 1);
	}

	public Ordenacao next() {
		return this.ordenacoes.get(this.iteracaoAtual++);
	}

	public void remove() {
		// TODO Auto-generated method stub
		
	}

	public Iterator<Ordenacao> iterator() {
		this.iteracaoAtual = 0;
		return this;
	}
	
}
