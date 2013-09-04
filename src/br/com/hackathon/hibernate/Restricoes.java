package br.com.hackathon.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;

import br.com.hackathon.util.Util;

/**
 * Essa classe se responsabiliza por organizar e gerar Alias de atributos compostos para que sejam criadas suas Restri��es.
 * H� uma limita��o, no entanto. A limita��o ocorre quando enviamos nomes com mais de uma composi��o, conforme segue o exemplo abaixo:
 * 
 * "itemPedido.cotacao.solicitacaoCotacao.cliente.nome".
 * 
 */
public class Restricoes implements Iterable<Restricao>, Iterator<Restricao>{
	
	private List<Restricao> restricoes;
	private Integer iteracaoAtual;
	
	public Restricoes(){
		this.restricoes = new ArrayList<Restricao>();
	}
	
	
	public Restricoes(Restricao ... restricoes) {
		this();
		for (Restricao r: restricoes){
			this.restricoes.add(r);
		}
	}
	
	public Restricoes(List<Restricao> restricoes) {
		
		this.restricoes = restricoes;
	}	
	
	/**
	 * Acumula todas as instancias de Restricao que estiverem dentro da lista de Restricoes recebida.
	 * @param r
	 */
	public Restricoes(Restricoes ... r) {
		this();
		for (Restricoes restricoes: r){
			for (Restricao restricao: restricoes){
				this.restricoes.add(restricao);
			}
		}
	}

	public void gerarRestricoes(Criteria criteria) {
		
		List<String> aliasCriados = new ArrayList<String>();
		
		if(restricoes != null) {
			for(Restricao restricao : restricoes) {
				if (Util.preenchido(restricao)){
					if(Util.preenchido(restricao.getNomeAtributo()) && restricao.getNomeAtributo().contains(".")) {
						restricao.setNomeAtributo(UtilHibernate.geraAlias(criteria, restricao.getNomeAtributo(), aliasCriados));
					}
					
					criteria.add(restricao.gerarRestricao());
				}
					
			}
		}
	}
	
	public Restricao[] getRestricoes(){
		return this.restricoes.toArray(new Restricao[]{});
	}
	
	public void removerRestricao(Restricao restricao, Restricoes restricoes){
		
		List<Restricao> restricoes2 = new ArrayList<Restricao>();
		
		for(Restricao restricao2: restricoes.getRestricoes()){
			if (Util.preenchido(restricao2)){
				if(!restricao2.equals(restricao)){
					restricoes2.add(restricao2);
				}
			}
		}

		if (Util.preenchido(restricoes2)){
			this.restricoes = restricoes2;
		}
		
		if(this.restricoes.size() == 1 && this.restricoes.get(0) == null){
			this.restricoes = new ArrayList<Restricao>();
		}
	}

	public Iterator<Restricao> iterator() {
		this.iteracaoAtual = 0;
		return this;
	}

	public boolean hasNext() {
		return Util.preenchido(this.restricoes) && (this.iteracaoAtual <= this.restricoes.size() - 1);
	}

	public Restricao next() {
		return this.restricoes.get(this.iteracaoAtual++);
	}

	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
