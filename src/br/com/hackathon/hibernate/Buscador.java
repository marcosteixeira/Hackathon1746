package br.com.hackathon.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import br.com.caelum.vraptor.Result;
import br.com.hackathon.modelo.auxiliar.ResultadoPesquisa;
import br.com.hackathon.modelo.entidade.Entidade;
import br.com.hackathon.util.Util;
import br.com.hackathon.util.UtilResult;

/**
 * Classe responsavel por todas as consultas feitas ao banco de dados. As consultas sao feitas montando dinamicamente um
 * filtro de busca baseado na Entidade passada como parametro.
 * 
 * @author dalton
 */

public class Buscador {

    private static final Integer PRIMEIRA_PAGINA = 1;
    public static final Integer VALOR_PADRAO_TOTAL_REGISTROS_POR_PAGINA = 10;
    
    public static final Boolean LIKE_HABILITADO = Boolean.TRUE;
    public static final Boolean LIKE_DESABILITADO = Boolean.FALSE;

	/**
     * Pesquisa que retorna o resultado *sempre* paginado, com um numero limite de registros por pagina.
     * 
     * @param filtro - Filtro usado para fazer a busca
     * @param pagina - Numero da pagina atual, onde o resultado "começa"
     * @param registrosPorpagina - Quantidade de registros por pagina
     * @param ordenacoes - Ordenacoes adicionais a serem aplicadas ao resultado
     * @return
     */
    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Integer pagina, Integer registrosPorpagina,
            Ordenacoes ordenacoes) {
        return pesquisar(filtro, pagina, registrosPorpagina, ordenacoes, MatchMode.ANYWHERE);
    }

    /**
     * Pesquisa passando apenas o filtro de busca.
     * Nesse caso, como a pesquisa será paginada, o número da página será assumido como sendo 1 e a quantidade
     * de registros por página será uma quantidade padrao.
     * @see Buscador.VALOR_PADRAO_TOTAL_REGISTROS_POR_PAGINA
     * 
     * @param usuarioComum
     * @return
     */
    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro) {
        return pesquisar(filtro, PRIMEIRA_PAGINA, VALOR_PADRAO_TOTAL_REGISTROS_POR_PAGINA, (Ordenacoes) null);
    }

    /**
     * Pesquisa que retorna o resultado com paginacao. Essa é a assinatura mais completa.
     * @param pagina número da página atual
     * @param totalRegistrosPorPagina quantidade de registros que serão exibidos por página
     * @param matchMode Seleciona que tipo de MatchMode será usado (ANYWHERE, EXTACT, etc)
     * @param projecao Define qual será o resultado da pesquisa
     */
    @SuppressWarnings("unchecked")
    public static <E extends Object> ResultadoPesquisa<E> pesquisar(final Entidade filtro, final Integer pagina,
            final Integer totalRegistrosPorPagina, final Ordenacoes ordenacoes, final MatchMode matchMode, 
            final Restricoes restricoes, final Boolean usaDistinctEntidadeRaiz, final JoinType joinType) {

        return (ResultadoPesquisa<E>) TemplateTransacao.executa(new TransacaoSomenteLeitura() {

            @Override
            public Object executaTransacao(Session sessaoAberta) {
                Integer registrosPorPagina = VALOR_PADRAO_TOTAL_REGISTROS_POR_PAGINA;

                @SuppressWarnings("rawtypes")
                List resultado = new ArrayList<E>();
                
                final Ordenacoes ordenacoesTratadas;
                
                if(Util.vazio(ordenacoes)) {
                    ordenacoesTratadas = new Ordenacoes(Ordenacao.decrescente(Entidade.ATRIBUTO_ID));
                } else {
                    ordenacoesTratadas = ordenacoes;
                }

                ResultadoPesquisa<E> resultadoPesquisa = new ResultadoPesquisa<E>();

                Criteria criteria = montaCriterioDaBusca(sessaoAberta, filtro, ordenacoesTratadas, matchMode, restricoes, joinType);
                /* cuida da paginação */
                adicionaPaginacao(criteria,pagina);
                if(Util.preenchido(totalRegistrosPorPagina)) {
                    registrosPorPagina = totalRegistrosPorPagina;
                }
                
                Criteria criteria2 = montaCriterioDaBusca(sessaoAberta, filtro, ordenacoes, matchMode, restricoes, joinType);
                
                criteria2.setMaxResults(registrosPorPagina);

                /* calcula a última página */
                int ultimaPagina = new Double(Math.ceil(resultadoPesquisa.getQtdeRegistros().doubleValue() / registrosPorPagina))
                        .intValue();

                /* cuida do inicio do resultado */
                if(Util.preenchido(pagina)) {
                    Integer paginaAtual = pagina;
                    if(pagina > ultimaPagina) {
                        paginaAtual = ultimaPagina;
                    }

                    int primeiroRegistroPagina = (paginaAtual - 1) * registrosPorPagina;
					criteria2.setFirstResult(primeiroRegistroPagina);
                }

                if(usaDistinctEntidadeRaiz) {
                	criteria2.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                }
                
                /* busca no banco os registros que obedecem a essa busca */
                resultado = criteria2.list();

                resultadoPesquisa.setResultado(resultado);

                return resultadoPesquisa;
            }
        });

    }
    
	private static void adicionaPaginacao(Criteria criteria, Integer pagina) {
		criteria.setProjection(Projections.rowCount());
		Long quantidadeRegistros = (Long) criteria.uniqueResult();

		Integer quantidadeDeRegistrosPorPagina = VALOR_PADRAO_TOTAL_REGISTROS_POR_PAGINA;
		if (pagina == null) {
			pagina = 1;
		}
		pagina = pagina - 1;
		Integer primeiroRegistroPagina = pagina * quantidadeDeRegistrosPorPagina;
		criteria.setFirstResult(primeiroRegistroPagina);
		criteria.setMaxResults(quantidadeDeRegistrosPorPagina);

		Result result = UtilResult.getResult();
		if (Util.preenchido(result)) {
			result.include("quantidadeRegistros", quantidadeRegistros);
			result.include("primeiroRegistroPagina", primeiroRegistroPagina + 1);
			result.include("ultimoRegistroPagina", primeiroRegistroPagina + quantidadeDeRegistrosPorPagina);
			result.include("pagina", pagina + 1);
			result.include("quantidadePaginas", quantidadeRegistros / quantidadeDeRegistrosPorPagina + 1);
		}
		
	}

    private static Criteria montaCriterioDaBusca(Session sessaoAberta, final Entidade filtro, final Ordenacoes ordenacoes,
            final MatchMode matchMode) {

        return montaCriterioDaBusca(sessaoAberta, filtro, ordenacoes, matchMode, null, JoinType.INNER_JOIN);
    }

    private static Criteria montaCriterioDaBusca(Session sessaoAberta, final Entidade filtro) {

        return montaCriterioDaBusca(sessaoAberta, filtro, null, MatchMode.EXACT);
    }

    /**
     * metodo que, baseado no filtro passado, monta um "criteria" (do hibernate). esse criteria será usado tanto na
     * busca, quanto na contagem de registros.
     * @param tipoJoin 
     * 
     * @return
     */
    private static Criteria montaCriterioDaBusca(Session sessaoAberta, final Entidade filtro, final Ordenacoes ordenacoes,
            final MatchMode matchMode, Restricoes restricoes, JoinType tipoJoin) {

        ArrayList<String> aliasCriados = new ArrayList<String>();
        String aliasEntidadeRaiz = Finder.geraAlias(filtro);
        Criteria createCriteria = sessaoAberta.createCriteria(filtro.getClass(), aliasEntidadeRaiz);

        /* Evita ciclo em relacao à  entidade "raiz" (onde a busca comeca) */
        aliasCriados.add(aliasEntidadeRaiz);

        Criteria criteria = Finder.criaFiltro(null, filtro, createCriteria, aliasCriados, matchMode, tipoJoin);

        if(Util.preenchido(ordenacoes) && !ordenacoes.vazio()) {
            for(Ordenacao o : ordenacoes.getOrdenacoes()) {
                String attr = o.getNomeAtributo();
                String alias = Finder.geraAlias(o.getEntidade());
                criteria.addOrder(o.gerarOrder(Finder.geraAssociacao(alias, attr)));
            }
        }

        if(Util.preenchido(restricoes)) {
            for(Restricao restricao : restricoes) {
                criteria.add(restricao.gerarRestricao());
            }
        }

        return criteria;
    }
    
    public static <E extends Object> List<E> listar(final Entidade filtro, final Ordenacoes ordenacoes, final Restricoes restricoes, final MatchMode matchMode,final Integer qtdeMaxRegistros, final JoinType tipoJoin) {
    	
    	return listar(filtro, ordenacoes, restricoes, matchMode, qtdeMaxRegistros, tipoJoin, null);
    	
    }

    @SuppressWarnings("unchecked")
    public
    static <E extends Object> List<E> listar(final Entidade filtro, final Ordenacoes ordenacoes, final Restricoes restricoes, final MatchMode matchMode, 
            final Integer qtdeMaxRegistros, final JoinType tipoJoin, final Boolean usaDistinctEntidadeRaiz) {

        return (List<E>) TemplateTransacao.executa(new TransacaoSomenteLeitura() {

            @Override
            public Object executaTransacao(Session sessaoAberta) {
                Criteria criteria = montaCriterioDaBusca(sessaoAberta, filtro, ordenacoes, matchMode, restricoes, tipoJoin);
                
                if(Util.preenchido(qtdeMaxRegistros)) {
                    criteria.setMaxResults(qtdeMaxRegistros);
                }
                
                if(Util.preenchido(usaDistinctEntidadeRaiz) && usaDistinctEntidadeRaiz) {
                	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                }
                
                return criteria.list();
            }
        });
		
	}

    
    
    public <E extends Entidade> List<E> listar2(E filtro){
    	return this.listarExecute(filtro);
    }
    
    @SuppressWarnings("unchecked")
    private <E extends Entidade> List<E> listarExecute(final E filtro) {

    	
    	final JoinType join = Util.preenchido(this.joinType) ? this.joinType : JoinType.INNER_JOIN;
    	final MatchMode mode = Util.preenchido(this.matchMode) ? this.matchMode : MatchMode.ANYWHERE;
    	final Boolean usaDistinctRaiz = Util.preenchido(this.usaDistinctEntidadeRaiz) ? true : false;
    	
    	return (List<E>) TemplateTransacao.executa(new TransacaoSomenteLeitura() {

            @Override
            public Object executaTransacao(Session sessaoAberta) {
                Criteria criteria = montaCriterioDaBusca(sessaoAberta, filtro, ordenacoes, mode, restricoes, join);
                
                if(Util.preenchido(qtdeRegistros)) {
                    criteria.setMaxResults(qtdeRegistros);
                }
                
                if(usaDistinctRaiz){
                	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                }
                
                
                return criteria.list();
            }
        });
		
	}

    @SuppressWarnings({ "rawtypes" })
	public List buscaPorHql(final String hql) {
    	
    	return (List) TemplateTransacao.executa(new TransacaoSomenteLeitura() {

            @Override
            public Object executaTransacao(Session sessaoAberta) {
            	
            	Query query = sessaoAberta.createQuery(hql);
                return query.list();
            }
        });
	}

    
    /**
     * 
     * Executa uma listagem comppleta, sem nenhum filtro ou paginação
     * @param <E>
     * @param classe Classe onde será criada o Criteria.
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E extends Entidade> List<E> listar(final Class<E> classe) {
        
        return (List<E>) TemplateTransacao.executa(new Transacao() {

            public Object executaTransacao(Session sessaoAberta) {
                
                Criteria criteria = sessaoAberta.createCriteria(classe);
                
                return criteria.list();
                
            }
        });
    }
    
    
    public static <E extends Entidade> List<E> listar(E filtro, Ordenacoes ordenacoes) {
        return listar(filtro, ordenacoes, (Restricoes) null, MatchMode.ANYWHERE,(Integer) null, JoinType.INNER_JOIN);
    }
    
    public <E extends Entidade> E ultimo(E filtro) {
    	return ultimo(filtro, new Ordenacoes(Ordenacao.decrescente(Entidade.ATRIBUTO_ID)));
	}
    
    public <E extends Entidade> E ultimo(E filtro, Ordenacoes ordenacoes) {
    	List<E> resultado = Buscador.pesquisar(filtro, 1,1, ordenacoes).getResultado();
		
    	if(Util.preenchido(resultado)){
    		return resultado.get(0) ;
    	}else {
			return null ;
		}
	} 
    
    public <E extends Entidade> E primeiro(E filtro) {
    	List<E> resultado = Buscador.pesquisar(filtro, 1,1, new Ordenacoes(Ordenacao.crescente(Entidade.ATRIBUTO_ID))).getResultado();
    	
    	if(Util.preenchido(resultado)){
    		return resultado.get(0) ;
    	}else {
    		return null ;
    	}
    } 

    public static <E extends Entidade> List<E> listar(E filtro, Ordenacoes ordenacoes, Integer qtdeMaxRegistros) {
        return listar(filtro, ordenacoes, (Restricoes) null, MatchMode.ANYWHERE, qtdeMaxRegistros, JoinType.INNER_JOIN);
    }
    
    public static <E extends Entidade> List<E> listar(E filtro, Restricoes restricoes, Integer qtdeMaxRegistros) {
    	return listar(filtro, null, restricoes, MatchMode.ANYWHERE, qtdeMaxRegistros, JoinType.INNER_JOIN);
    }
    
    public static <E extends Entidade> List<E> listar(E filtro, Ordenacoes ordenacoes, Restricoes restricoes, Boolean usaDistinctEntidadeRaiz) {
    	return listar(filtro, ordenacoes, restricoes, MatchMode.ANYWHERE,(Integer) null, JoinType.INNER_JOIN, usaDistinctEntidadeRaiz);
    }
    
    public static <E extends Object> List<E> listar(Entidade filtro, Ordenacoes ordenacoes, Restricoes restricoes, Integer qtdeMaxRegistros) {
    	return listar(filtro, ordenacoes, restricoes, MatchMode.ANYWHERE, qtdeMaxRegistros, JoinType.INNER_JOIN);
    }
    
    public static <E extends Entidade> List<E> listar(E filtro, Ordenacoes ordenacoes, Restricoes restricoes) {
        return listar(filtro, ordenacoes, restricoes, MatchMode.ANYWHERE, null, JoinType.INNER_JOIN);
    }
    
    public static <E extends Entidade> List<E> listar(E filtro, Restricoes restricoes) {
    	return listar(filtro, (Ordenacoes) null, restricoes, MatchMode.ANYWHERE, null, JoinType.INNER_JOIN);
    }
    
    public static <E extends Object> List<E> listar(final Entidade filtro) {
        return listar(filtro, (Ordenacoes) null, (Restricoes) null, MatchMode.ANYWHERE,  null, JoinType.INNER_JOIN);
    }
    
    public static <E extends Object> List<E> listar(final Entidade filtro,  final Integer qtdeMaxRegistros) {
    	return listar(filtro, (Ordenacoes) null, (Restricoes) null, MatchMode.ANYWHERE,qtdeMaxRegistros, JoinType.INNER_JOIN);
    }
    
    public static <E extends Object> List<E> listar(Entidade filtro, Ordenacoes ordenacoes, MatchMode matchmode) {
    	return listar(filtro, ordenacoes, (Restricoes) null, matchmode, null, JoinType.INNER_JOIN);
    }
    
    public static <E extends Object> List<E> listar(Entidade filtro,  Restricoes restricoes , MatchMode matchmode) {
    	return listar(filtro, (Ordenacoes)null, restricoes, matchmode, null, JoinType.INNER_JOIN);
    }
    
    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Integer pagina) {
        return pesquisar(filtro, pagina, VALOR_PADRAO_TOTAL_REGISTROS_POR_PAGINA, (Ordenacoes) null);
    }

    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Restricoes restricoes) {
        return pesquisar(filtro, null, restricoes);
    }
    
    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Integer pagina, Integer totalRegistrosPorPagina) {
    	return pesquisar(filtro, pagina, totalRegistrosPorPagina, (Ordenacoes) null);
    }
    
    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Integer pagina, Integer totalRegistrosPorPagina, Restricoes restricoes) {
    	return pesquisar(filtro, pagina, totalRegistrosPorPagina, (Ordenacoes) null, MatchMode.ANYWHERE, restricoes, Boolean.FALSE, JoinType.INNER_JOIN);
    }
    
    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Integer pagina, Ordenacoes ordenacoes) {
        return pesquisar(filtro, pagina, VALOR_PADRAO_TOTAL_REGISTROS_POR_PAGINA, ordenacoes);
    }

    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Integer pagina, Ordenacoes ordenacoes, Restricoes restricoes) {
        return pesquisar(filtro, pagina, null, ordenacoes, MatchMode.ANYWHERE, restricoes,  Boolean.FALSE, JoinType.INNER_JOIN);
    }

    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Integer pagina, Integer totalRegistrosPorPagina,
            Ordenacoes ordenacoes, MatchMode matchMode) {
        return pesquisar(filtro, pagina, totalRegistrosPorPagina, ordenacoes, matchMode, null, Boolean.FALSE, JoinType.INNER_JOIN);
    }
    
    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Integer pagina, Integer totalRegistrosPorPagina,
    		Ordenacoes ordenacoes, Restricoes restricoes) {
    	return pesquisar(filtro, pagina, totalRegistrosPorPagina, ordenacoes, MatchMode.ANYWHERE, restricoes, Boolean.FALSE, JoinType.INNER_JOIN);
    }
    
    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Integer pagina, Integer totalRegistrosPorPagina,
    		Ordenacoes ordenacoes, Restricoes restricoes, Boolean usaDistinctEntidadeRaiz) {
    	return pesquisar(filtro, pagina, totalRegistrosPorPagina, ordenacoes, MatchMode.ANYWHERE, restricoes, usaDistinctEntidadeRaiz, JoinType.INNER_JOIN);
    }
    
    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Integer pagina, Integer totalRegistrosPorPagina,
    		Ordenacoes ordenacoes, Boolean usaDistinctEntidadeRaiz) {
    	return pesquisar(filtro, pagina, totalRegistrosPorPagina, ordenacoes, MatchMode.ANYWHERE, null, usaDistinctEntidadeRaiz, JoinType.INNER_JOIN);
    }

    public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E filtro, Integer pagina, Integer totalRegistrosPorPagina,
            Ordenacoes ordenacoes, Restricoes restricoes, Boolean usaDistinctEntidadeRaiz, JoinType joinType) {
        return pesquisar(filtro, pagina, totalRegistrosPorPagina, ordenacoes, MatchMode.ANYWHERE, restricoes,  usaDistinctEntidadeRaiz, joinType);
    }

    /**
     * Faz uma contagem do total de registros retornados por uma listagem obedecendo o filtro passado
     * como parametro. Esse método faz a contagem via "count(*)" direto no banco e nao via resultado.size().
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     * @return o número de registros, ou zero caso não haja registro
     */
    public static Long contarElementos(final Entidade filtro) {
    	
    	TransacaoSomenteLeitura transacaoSomenteLeitura = gerarTransacaoContagem(filtro);
    	
        return (Long) TemplateTransacao.executa(transacaoSomenteLeitura);
    }
    
    /**
     * Faz uma contagem do total de registros retornados por uma listagem obedecendo o filtro passado
     * como parametro. Esse método faz a contagem via "count(*)" usando distinct direto no banco e nao via resultado.size().
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     * @param campo campo que será usado para fazer o distinct
     * @return o número de registros, ou zero caso não haja registro
     */
    public static Integer contarElementosComDistinct(final Entidade filtro, String campo) {
    	
    	TransacaoSomenteLeitura transacaoSomenteLeitura = gerarTransacaoContagem(filtro, campo);
    	
        return (Integer) TemplateTransacao.executa(transacaoSomenteLeitura);
    }
    
    /**
     * Faz uma contagem do total de registros retornados por uma listagem obedecendo o filtro passado
     * como parametro. Esse método faz a contagem via "count(*)" usando distinct direto no banco e nao via resultado.size().
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     * @param campo campo que será usado para fazer o distinct
     * @return o número de registros, ou zero caso não haja registro
     */
    public static Integer contarElementosComDistinct(final Entidade filtro, final Restricoes restricoes, String campo) {
    	
    	TransacaoSomenteLeitura transacaoSomenteLeitura = gerarTransacaoContagem(filtro, restricoes, campo);
    	
    	return (Integer) TemplateTransacao.executa(transacaoSomenteLeitura);
    }
    
    /**
     * Faz uma contagem do total de registros retornados por uma listagem obedecendo o filtro passado
     * como parametro. Esse método faz a contagem via "count(*)" direto no banco e nao via resultado.size().
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     * @param likeHabilitado Flag que dirá se queremos uma busca por elementos com LIKE ou não.
     */
    public static Integer contarElementos(final Entidade filtro, final Boolean likeHabilitado) {
    	
    	TransacaoSomenteLeitura transacaoSomenteLeitura = gerarTransacaoContagem(filtro, likeHabilitado);
    	
    	return (Integer) TemplateTransacao.executa(transacaoSomenteLeitura);
    }
    
    /**
     * Faz uma contagem do total de registros retornados por uma listagem obedecendo o filtro passado
     * como parametro. Esse método faz a contagem via "count(*)" direto no banco e nao via resultado.size().
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     * @param restricoes Restrições extras que serão consideradas na busca e não puderam ser expressadas pelos valores das propriedades da Entidade-Filtro.
     */
    public static Long contarElementos(final Entidade filtro, final Restricoes restricoes) {
    	
    	TransacaoSomenteLeitura transacaoSomenteLeitura = gerarTransacaoContagem(filtro, restricoes);
    	
    	return (Long) TemplateTransacao.executa(transacaoSomenteLeitura);
    }
    
    /**
     * Gera uma transacao para fazer contagem de elementos no banco.
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     */
    private static TransacaoSomenteLeitura gerarTransacaoContagem(final Entidade filtro) {
    	return gerarTransacaoContagem(filtro, LIKE_HABILITADO, null, null);
    }
    
    /**
     * Gera uma transacao para fazer contagem de elementos no banco.
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     * @param campo campo que será usado para fazer o distinct
     */
    private static TransacaoSomenteLeitura gerarTransacaoContagem(final Entidade filtro, final String campo) {
    	return gerarTransacaoContagem(filtro, LIKE_HABILITADO, null, campo);
    }
    
    /**
     * Gera uma transacao para fazer contagem de elementos no banco.
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     * @param restricoes Restrições extras que serão consideradas na busca e não puderam ser expressadas pelos valores das propriedades da Entidade-Filtro.
     * @param campo campo que será usado para fazer o distinct
     */
    private static TransacaoSomenteLeitura gerarTransacaoContagem(final Entidade filtro, final Restricoes restricoes, final String campo) {
    	return gerarTransacaoContagem(filtro, LIKE_HABILITADO, restricoes, campo);
    }
    
    /**
     * Gera uma transacao para fazer contagem de elementos no banco.
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     * @param restricoes Restrições extras que serão consideradas na busca e não puderam ser expressadas pelos valores das propriedades da Entidade-Filtro.
     */
    private static TransacaoSomenteLeitura gerarTransacaoContagem(final Entidade filtro, final Restricoes restricoes) {
    	return gerarTransacaoContagem(filtro, LIKE_HABILITADO, restricoes, null);
    }
    
    /**
     * Gera uma transacao para fazer contagem de elementos no banco.
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     * @param likeHabilitado Flag que dirá se queremos uma busca por elementos com LIKE ou não.
     */
    private static TransacaoSomenteLeitura gerarTransacaoContagem(final Entidade filtro, final Boolean likeHabilitado) {
    	return gerarTransacaoContagem(filtro, likeHabilitado, null, null);
    }
    
    /**
     * Gera uma transacao para fazer contagem de elementos no banco.
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     * @param likeHabilitado Flag que dirá se queremos uma busca por elementos com LIKE ou não.
     * @param restricoes Restrições extras que serão consideradas na busca e não puderam ser expressadas pelos valores das propriedades da Entidade-Filtro.
     * @param campo Campo que será usado para fazer o distinct na contagem. Caso seja nulo, não faz distinct.
     */
    private static TransacaoSomenteLeitura gerarTransacaoContagem(final Entidade filtro, final Boolean likeHabilitado, final Restricoes restricoes, final String campo) {
    	
    	return new TransacaoSomenteLeitura() {
        	
            @Override
            public Object executaTransacao(Session sessaoAberta) {
                Criteria criteria = montaCriterioDaBusca(sessaoAberta, filtro, (Ordenacoes) null,
                		(LIKE_HABILITADO.equals(likeHabilitado) ? MatchMode.ANYWHERE : MatchMode.EXACT),
                		restricoes, JoinType.INNER_JOIN);                
                 
                 if(Util.preenchido(campo)) {
                	 criteria.setProjection(Projections.countDistinct(campo));
                 } else {
                	 criteria.setProjection(Projections.rowCount());
                 }
                 
                return (Long) criteria.list().get(0);
            }
        };
    }
    
	public static <E extends Entidade> ResultadoPesquisa<E> pesquisar(E produto,
			Ordenacoes ordenacoes, Restricoes restricoes) {
		return pesquisar(produto, null, ordenacoes, restricoes);
	}

	/**
	 * Faz uma query usando o filtro passado e adiciona um Projections.max(coluna)
	 * usando o nome da coluna passada como parametro.
	 * @param coluna
	 * @param filtro
	 * @return
	 */
	public static Integer valorMaximo(final String coluna, final Entidade filtro) {
		Integer valorMaximo = (Integer) TemplateTransacao.executa(retornarTransacaoValorMaximo(coluna, filtro));
			
		if (Util.vazio(valorMaximo)){
			return new Integer(0);
		}
		
		return (Integer) valorMaximo;
	}

	/**
	 * 
	 * Retorna a transacao usada na query que usa Projections.max();
	 * @param coluna
	 * @param filtro
	 * @return
	 */
	private static TransacaoSomenteLeitura retornarTransacaoValorMaximo(final String coluna, final Entidade filtro) {
		return new TransacaoSomenteLeitura() {
			
			@Override
			public Object executaTransacao(Session sessaoAberta) {
				Criteria criteria = montaCriterioDaBusca(sessaoAberta, filtro);

				criteria.setProjection(Projections.max(coluna));
				return (Integer) criteria.list().get(0);
				
			}
		};
	}

	
	/**
	 * Faz a soma dos valores que estão no campo passado como parâmetro para todos os registros retornados
	 * por uma listagem obedecendo o filtro passado.
	 * 
	 * OBS.: Caso a busca não encontre nenhum resultado o método retorna zero(0).
	 * 
	 * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
	 * @param nomeCampo String que representa o campo que terá os seus valores somados.
	 */
	public static BigDecimal somarValores(final Entidade filtro, String nomeCampo) {
		
		return somarValores(filtro, nomeCampo, null);
	}
	/**
	 * Faz a soma dos valores que estão no campo passado como parâmetro para todos os registros retornados
	 * por uma listagem obedecendo o filtro passado.
	 * 
	 * OBS.: Caso a busca não encontre nenhum resultado o método retorna zero(0).
	 * 
	 * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
	 * @param nomeCampo String que representa o campo que terá os seus valores somados.
	 * @param restricoes Restrições extras que serão consideradas na busca e não puderam ser expressadas pelos valores das propriedades da Entidade-Filtro.
	 */
	public static BigDecimal somarValores(final Entidade filtro, String nomeCampo, Restricoes restricoes) {
		
		TransacaoSomenteLeitura transacaoSomenteLeitura = gerarTransacaoSoma(filtro, LIKE_HABILITADO, restricoes, nomeCampo);
		
		Object resultado = TemplateTransacao.executa(transacaoSomenteLeitura);
		
		if(Util.preenchido(resultado)){
			
			return new BigDecimal(resultado.toString());
			
		}else{
			return BigDecimal.ZERO;
		}
	}
	
	/**
     * Gera uma transação para fazer a soma dos valores do atributo passado como parâmetro
     * 
     * @param filtro Entidade de exemplo que terá suas propriedades consideradas na busca por entidades semelhantes no banco.
     * @param likeHabilitado Flag que dirá se queremos uma busca por elementos com LIKE ou não.
     * @param restricoes Restrições extras que serão consideradas na busca e não puderam ser expressadas pelos valores das propriedades da Entidade-Filtro.
     * @param nomeCampo String que representa o campo que terá os seus valores somados.
     */
    private static TransacaoSomenteLeitura gerarTransacaoSoma(final Entidade filtro, final Boolean likeHabilitado, 
    		final Restricoes restricoes, final String nomeCampo) {
    	
    	return new TransacaoSomenteLeitura() {
        	
            @Override
            public Object executaTransacao(Session sessaoAberta) {
                Criteria criteria = montaCriterioDaBusca(sessaoAberta, filtro, (Ordenacoes) null,
                		(LIKE_HABILITADO.equals(likeHabilitado) ? MatchMode.ANYWHERE : MatchMode.EXACT),
                		restricoes, JoinType.INNER_JOIN);

                criteria.setProjection(Projections.sum(nomeCampo));
                
				return criteria.list().get(0);
            }
        };
    }
	
    public static <E extends Entidade> List<E> listar(E filtro, Restricoes restricoes, JoinType tipoJoin) {
        return listar(filtro, null, restricoes,  null, null, tipoJoin);
    }
    
    public static <E extends Entidade> List<E> listar(E filtro, Ordenacoes ordenacoes, Restricoes restricoes, JoinType tipoJoin) {
        return listar(filtro, ordenacoes, restricoes,null, null, tipoJoin);
    }


    /*Atributos do Builder*/

    private Restricoes restricoes;
    private Ordenacoes ordenacoes;
    private JoinType joinType;
    private MatchMode matchMode;
    private Integer qtdeRegistros;
    private Boolean usaDistinctEntidadeRaiz;
    
    /*Métodos do Builder*/

    public <E extends Entidade> E selecionar(E filtro) {
		return selecionarExecute(filtro);
	}
	
    /*Métodos auxiliares, usados pelos métodos do Builder*/

    /**
	 * Precisamos juntar todas as restricoes recebidas por parametro.
	 * @param restricoes
	 */
	public Buscador restricoes(Restricoes ... restricoes) {
		List<Restricoes> listRestricao = new ArrayList<Restricoes>();
		
		
		if (Util.preenchido(this.restricoes)){
			listRestricao.add(this.restricoes);
		}

		for (Restricoes r: restricoes){
			listRestricao.add(r);
		}
		
		this.restricoes = new Restricoes(listRestricao.toArray(new Restricoes[]{}));
		return this;
	}

	
	public Buscador ordenacoes(Ordenacoes ... ordenacoes){
		List<Ordenacoes> ordenacoesList = new ArrayList<Ordenacoes>();
		
		if (Util.preenchido(this.ordenacoes)){
			ordenacoesList.add(this.ordenacoes);
		}
		
		for (Ordenacoes o: ordenacoes){
			ordenacoesList.add(o);
		}
		
		this.ordenacoes = new Ordenacoes(ordenacoesList.toArray(new Ordenacoes[]{}));
		
		return this;
	}
	
	
	/**
	 * Adiciona um Join específico. O join padrão usado é sempre JoinType.INNER_JOIN
	 * @param join
	 * @return
	 */
	public Buscador join(JoinType join) {
		this.joinType = join;
		return this;
	}

	public Buscador distinctEntidadeRaiz(){
		this.usaDistinctEntidadeRaiz = true;
		return this;
	}
	
	/**
     * Esse metodo espera sempre um resultado unico. Lança uma exceção caso sejam encontrados
     * mais de um resultado para o filtro escolhido
     */
    @SuppressWarnings("unchecked")
	private <E extends Entidade> E selecionarExecute(final E filtro) {
    	
        return (E) TemplateTransacao.executa(new TransacaoSomenteLeitura(){
            
            @Override
            public Object executaTransacao(Session sessaoAberta) {
                Criteria criteria = montaCriterioDaBusca(sessaoAberta, filtro); 
                
                if(Util.preenchido(restricoes)) {
                    for(Restricao restricao : restricoes) {
                        criteria.add(restricao.gerarRestricao());
                    }
                }
                
                try {
                    return criteria.uniqueResult();
                    
                } catch (NonUniqueResultException e) {
                    throw new RuntimeException("O resultado não é único para o objeto da classe " + filtro.getClass().getSimpleName());
                }
            }});
    }

	/**
	 * Usado exclusivamente nos testes.
	 * @return
	 */
	public Restricoes getRestricoes() {
		return restricoes;
	}

	/**
	 * Usado exclusivamente nos testes
	 * @return
	 */
	public JoinType getJoinType() {
		return joinType;
	}

	/**
	 * Usado exclusivamente nos testes
	 * @return
	 */
	public Ordenacoes getOrdenacoes() {
		return ordenacoes;
	}

	/**
	 * Usado exclusivamente nos testes
	 * @return
	 */
	public MatchMode getMatchMode() {
		return matchMode;
	}

	public Buscador matchmode(MatchMode matchMode) {
		this.matchMode = matchMode;
		return this;
	}

	public Buscador qtdeRegistros(Integer maxResult) {
		this.qtdeRegistros = maxResult;
		return this;
	}

	public Integer getQtdeRegistros() {
		return qtdeRegistros;
	}
}
