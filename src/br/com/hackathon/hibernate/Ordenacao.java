package br.com.hackathon.hibernate;

import org.hibernate.criterion.Order;

import br.com.hackathon.modelo.entidade.Entidade;

/**
 * Classe respons�vel pela interface com a classe Order do Hibernate 
 */
public class Ordenacao {
	
	private String nomeAtributo;
	private boolean crescente;
	
	/**
	 * Entidade que possui o atributo que esta sendo referenciado na ordenacao;
	 */
	private Class<? extends Entidade> entidade;
	
	/**
	 * Construtor
	 *  
	 * @param nomeAtributo Nome do atributo da classe que indicar� o crit�rio de ordena��o
	 * @param crescente indica se a ordena��o ser� por ordem crescente ou n�o (decrescente)
	 * @param entidade TODO
	 */
	private Ordenacao(String nomeAtributo, boolean crescente, Class<? extends Entidade> entidade) {
		this.nomeAtributo = nomeAtributo;
		this.crescente = crescente;
		this.entidade = entidade;
	}
	
	/**
	 * Utilizado para ordem crescente
	 * 
	 * @param nomeAtributo
	 * @return Ordenacao
	 */
	public static Ordenacao crescente(String nomeAtributo) {
		return new Ordenacao(nomeAtributo, true, null);
	}
	
	/**
	 * Utilizado para ordem crescente por um atributo do atributo da classe utilizada
	 * @param nomeAtributo
	 * @param entidade
	 * @return
	 */
	public static Ordenacao crescente(final String nomeAtributo, final Entidade entidade) {
	    return crescente(criarNomeComAlias(nomeAtributo, entidade));
	}

	/**
	 * Utilizado para ordem decrescente
	 * 
	 * @param nomeAtributo
	 * @return Ordenacao
	 */
	public static Ordenacao decrescente(String nomeAtributo) {
		return new Ordenacao(nomeAtributo, false, null);
	}

	/**
	 * Utilizado para ordem decrescente por um atributo do atributo da classe utilizada
	 * 
	 * @param nomeAtributo
	 * @return Ordenacao
	 */
	public static Ordenacao decrescente(String nomeAtributo, final Entidade entidade) {
	    return decrescente(criarNomeComAlias(nomeAtributo, entidade));
	}
	
    /**
     * Retorna o nome j� com o alias para que possa ser feitas ordena��es complexas
     * @param nomeAtributo
     * @param entidade
     * @return
     */
    private static String criarNomeComAlias(final String nomeAtributo, final Entidade entidade) {
        
        final String alias = Finder.geraAlias(entidade);
        final String nomeComAlias = Finder.geraAssociacao(alias, nomeAtributo);
        
        return nomeComAlias;
    }
	
	/**
	 * Retorna um objeto Order (org.hibernate.criterion.Order), que o Hibernate
	 * utiliza para ordenar um criteria.
	 */
	public Order gerarOrder() {
		return gerarOrder(nomeAtributo);
	}
	
	public Order gerarOrder(String attr){
		if(crescente) {
			return Order.asc(attr);
		} else {
			return Order.desc(attr);
		}
	}
	
	
	/** 
	 * Define o atributo utilizado na ordena��o
	 * @param nomeAtributo Nome do atributo utilizado na ordena��o
	 */
	public void setNomeAtributo(String nomeAtributo) {
	
		this.nomeAtributo = nomeAtributo;
	}

	/**
	 * Retorna o nome do atributo utilizado na ordena��o
	 */
	public String getNomeAtributo() {
	
		return nomeAtributo;
	}

	public Class<? extends Entidade> getEntidade() {
		return entidade;
	}

	public void setEntidade(Class<? extends Entidade> entidade) {
		this.entidade = entidade;
	}

	public boolean isCrescente() {
		return crescente;
	}
}
