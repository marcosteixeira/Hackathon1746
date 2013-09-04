package br.com.hackathon.hibernate;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.hackathon.util.Util;

/**
 * Classe respons�vel pela interface com a classe Restrictions do Hibernate.
 */
public class Restricao {

	private static String EXCECAO_METODO_GERAR = "O tipo de crit�rio passado demanda a utiliza��o do outro m�todo \"Restricao.gerar()\".";

	private String nomeAtributo;
	private NomeAtributo nomeAtributo1; // atributo usado apenas na compara��o
										// entre duas colunas da entidade
	private NomeAtributo nomeAtributo2; // atributo usado apenas na compara��o
										// entre duas colunas da entidade
	private Object valorAtributo;
	private Object valorAtributoMinimo;
	private Object valorAtributoMaximo;
	private CriterioRestricao criterio;
	private MatchMode matchMode;

	private Restricao restricaoOr;
	private List<? extends Object> valoresRestricaoIn;

	/**
	 * Construtor
	 * 
	 * @param nomeAtributo
	 *            Nome do atributo da classe que indicar� o crit�rio de
	 *            restri��o.
	 * @param criterio
	 *            Indica o tipo de crit�rios utilizado na restri��oo.
	 */
	private Restricao(String nomeAtributo, Object valorAtributo, CriterioRestricao criterio, MatchMode matchMode) {

		this.nomeAtributo = nomeAtributo;
		this.valorAtributo = valorAtributo;
		this.criterio = criterio;
		this.matchMode = Util.preenchido(matchMode) ? matchMode : MatchMode.ANYWHERE;

	}

	private Restricao(NomeAtributo nomeAtributo1, NomeAtributo nomeAtributo2, CriterioRestricao criterio) {

		this.nomeAtributo1 = nomeAtributo1;
		this.nomeAtributo2 = nomeAtributo2;
		this.criterio = criterio;
	}
	
	private Restricao(String nomeAtributo, Object valorAtributoMinimo, Object valorAtributoMaximo, CriterioRestricao criterio) {
	    
	    this.nomeAtributo = nomeAtributo;
	    this.valorAtributoMinimo = valorAtributoMinimo;
	    this.valorAtributoMaximo = valorAtributoMaximo;
	    this.criterio = criterio;
	}

	/**
	 * Construtor
	 * 
	 * @param restricao1
	 *            Restri��o com os crit�rios que servir�o para a primeira parte
	 *            do OR.
	 * @param restricao2
	 *            Restri��o com os crit�rios que servir�o para a segunda parte
	 *            do OR.
	 */
	private Restricao(Restricao restricao1, Restricao restricao2) {

		this.nomeAtributo = restricao1.nomeAtributo;
		
		if(restricao1.criterio.equals(CriterioRestricao.BETWEEN)) {
			this.valorAtributoMinimo = restricao1.valorAtributoMinimo;
			this.valorAtributoMaximo = restricao1.valorAtributoMaximo;

		} else {
			this.valorAtributo = restricao1.valorAtributo;
		}
		this.criterio = restricao1.criterio;
		this.matchMode = restricao1.matchMode;
		this.restricaoOr = restricao2;
	}

	/**
	 * Construtor usado no caso do crit�rio de restricao IN
	 * 
	 * @param nomeAtributo
	 * @param valoresAtributo
	 */
	private Restricao(String nomeAtributo, CriterioRestricao criterio, List<? extends Object> valoresAtributo) {

		this.nomeAtributo = nomeAtributo;
		this.valoresRestricaoIn = valoresAtributo;
		this.criterio = criterio;
	}

	public static Restricao gerarOr(Restricao restricao1, Restricao restricao2) {
		return new Restricao(restricao1, restricao2);
	}

	/**
	 * M�todo usado para gerar restri��es "in"
	 * 
	 * @param nome
	 * @param valores
	 * @return
	 */
	public static Restricao gerarIn(String nome, List<? extends Object> valores) {
		if (valores.size() > 0) {
			return new Restricao(nome, CriterioRestricao.IN, valores);
		} else {
			throw new RuntimeException("O uso do crit�rio 'in' necessita de ter pelo menos um valor na lista de valores passados por par�metro.");
		}
	}

	/**
	 * M�todo usado para gerar restri��es "not in"
	 * 
	 * @param nome
	 * @param valores
	 * @return
	 */
	public static Restricao gerarNotIn(String nome, List<? extends Object> valores) {
		if (valores.size() > 0) {
			return new Restricao(nome, CriterioRestricao.NOT_IN, valores);
		} else {
			throw new RuntimeException("O uso do crit�rio 'in' necessita de ter pelo menos um valor na lista de valores passados por par�metro.");
		}
	}

	public static Restricao gerar(String nome, Object valor, CriterioRestricao criterioLike, MatchMode matchMode) {
		switch (criterioLike) {
		case EQ:
		case NE:
		case GE:
		case GT:
		case LE:
		case LT:
		case LIKE:
		case LIKE_NOT_STRING:
			return new Restricao(nome, valor, criterioLike, matchMode);

		default:
			throw new RuntimeException(EXCECAO_METODO_GERAR);
		}
	}

	public static Restricao gerar(String nome, Object valor, CriterioRestricao criterio) {

		return gerar(nome, valor, criterio, MatchMode.ANYWHERE);
	}

	public static Restricao gerar(NomeAtributo nomeatributo1, NomeAtributo nomeatributo2, CriterioRestricao criterio) {

		return new Restricao(nomeatributo1, nomeatributo2, criterio);
	}

	public static Restricao gerar(String nome, CriterioRestricao criterio) {

		switch (criterio) {
		case IS_EMPTY:
		case IS_NOT_EMPTY:
		case IS_NULL:
		case IS_NOT_NULL:
			return new Restricao(nome, (Object) null, criterio, (MatchMode) null);
		default:
			throw new RuntimeException(EXCECAO_METODO_GERAR);
		}
	}
	
	public static Restricao gerar(String nome, Object valorMinimo, Object valorMaximo, CriterioRestricao criterio) {
	    
	    switch(criterio) {
        case BETWEEN:
            return new Restricao(nome, valorMinimo, valorMaximo, criterio); 

        default:
            throw new RuntimeException(EXCECAO_METODO_GERAR);
        }
	}

	/**
	 * Retorna um objeto SimpleExpression
	 * (org.hibernate.criterion.SimpleExpression), que o Hibernate utiliza para
	 * restringir a busca.
	 */
	public Criterion gerarRestricao() {

		Criterion criterion = null;

		switch (criterio) {
		case IS_EMPTY:
			criterion = Restrictions.isEmpty(this.nomeAtributo);
			break;
		case IS_NOT_EMPTY:
			criterion = Restrictions.isNotEmpty(this.nomeAtributo);
			break;
		case IS_NULL:
			criterion = Restrictions.isNull(this.nomeAtributo);
			break;
		case IS_NOT_NULL:
			criterion = Restrictions.isNotNull(this.nomeAtributo);
			break;
		case EQ_PROPERTY:
			criterion = Restrictions.eqProperty(this.nomeAtributo1.retornaAtributoEntidade(), this.nomeAtributo2.retornaAtributoEntidade());
			break;
		case LE_PROPERTY:
			criterion = Restrictions.leProperty(this.nomeAtributo1.retornaAtributoEntidade(), this.nomeAtributo2.retornaAtributoEntidade());
			break;
		case GT_PROPERTY:
			criterion = Restrictions.gtProperty(this.nomeAtributo1.retornaAtributoEntidade(), this.nomeAtributo2.retornaAtributoEntidade());
			break;
		case EQ:
			criterion = Restrictions.eq(this.nomeAtributo, this.valorAtributo);
			break;
		case NE_PROPERTY:
			criterion = Restrictions.neProperty(this.nomeAtributo1.retornaAtributoEntidade(), this.nomeAtributo2.retornaAtributoEntidade());
			break;
		case NE:
			criterion = Restrictions.ne(this.nomeAtributo, this.valorAtributo);
			break;
		case GE:
			criterion = Restrictions.ge(this.nomeAtributo, this.valorAtributo);
			break;
		case GT:
			criterion = Restrictions.gt(this.nomeAtributo, this.valorAtributo);
			break;
		case LE:
			criterion = Restrictions.le(this.nomeAtributo, this.valorAtributo);
			break;
		case LT:
			criterion = Restrictions.lt(this.nomeAtributo, this.valorAtributo);
			break;
		case LIKE:
			criterion = Restrictions.like(this.nomeAtributo, (String) (this.valorAtributo), this.matchMode);
			break;
		case LIKE_NOT_STRING:
			criterion = Restrictions.like(this.nomeAtributo, this.valorAtributo);
			break;
		case IN:
			criterion = Restrictions.in(this.nomeAtributo, this.valoresRestricaoIn);
			break;
		case NOT_IN:
			criterion = Restrictions.not(Restrictions.in(this.nomeAtributo, this.valoresRestricaoIn));
			break;
		case BETWEEN:
		    criterion = Expression.between(this.nomeAtributo, this.valorAtributoMinimo, this.valorAtributoMaximo);
			break;
		default:
			throw new RuntimeException("O tipo de crit�rio de restri��o passado n�o � suportado. Cheque a classe CriterioRestricao.");
		}

		if (this.restricaoOr != null) {
			criterion = Restrictions.or(criterion, this.restricaoOr.gerarRestricao());
		}

		return criterion;
	}

	/**
	 * Define o atributo utilizado na restri��o.
	 * 
	 * @param nomeAtributo
	 *            Nome do atributo utilizado na restri��o.
	 */
	public void setNomeAtributo(String nomeAtributo) {

		this.nomeAtributo = nomeAtributo;
	}

	/**
	 * Retorna o nome do atributo utilizado na restri��o.
	 */
	public String getNomeAtributo() {

		return nomeAtributo;
	}
	
    public Object getValorAtributoMinimo() {
        return valorAtributoMinimo;
    }

    public Object getValorAtributoMaximo() {
        return valorAtributoMaximo;
    }

    /**
	 * Retorna o tipo de crit�rio usado na restri��o.
	 */
	public CriterioRestricao getCriterio() {
		return criterio;
	}

	/**
	 * Define o tipo de crit�rio utilizado na restri��o.
	 * 
	 * @param criterio
	 *            Enumerador utilizado para reconhecer o tipo de crit�rio da
	 *            restri��o.
	 */
	public void setCriterio(CriterioRestricao criterio) {
		this.criterio = criterio;
	}

	public MatchMode getMatchMode() {
		return matchMode;
	}

	@Override
	public boolean equals(Object object) {

		Restricao restricao;

		if (object != null && object instanceof Restricao) {

			restricao = (Restricao) object;

			try {
				if (restricao.criterio.equals(this.criterio) && restricao.nomeAtributo.equals(this.nomeAtributo) && restricao.valorAtributo.equals(this.valorAtributo)) {

					return true;
				}
			} catch (NullPointerException npe) {

				npe.printStackTrace();
			}
		}

		return false;
	}

}