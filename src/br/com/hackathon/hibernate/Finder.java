package br.com.hackathon.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import br.com.hackathon.modelo.entidade.Entidade;
import br.com.hackathon.util.Util;
import br.com.hackathon.util.UtilReflect;

public class Finder {

    private static Configuration hbConfiguration;
    private static final PropertySelector seletor = new SeletorPropriedades();

    public Finder() {
        super();
    }

    /**
     * Retorna o nome dos atributos mapeados, MENOS o nome da chave primaria.
     * 
     * Esse m�todo s� retorna os atributos at� 2 n�veis a partir da entidade passada.
     * 
     * @param entidade
     * @return
     */
    
    @SuppressWarnings("unchecked")
	public static ArrayList<String> nomesAtributosMapeados(Entidade entidade) {

        ArrayList<String> atributos = new ArrayList<String>();
        Configuration config = getHibernateConfiguration();
		PersistentClass classMapping = config.getClassMapping(entidade.getClass().getCanonicalName());
        Iterator<Property> propertyIterator = classMapping.getPropertyIterator();

        if(classMapping.isInherited()) {
            Iterator<Property> superClassPropertyIterator = config.getClassMapping(
                    entidade.getClass().getSuperclass().getCanonicalName()).getPropertyIterator();

            while(superClassPropertyIterator.hasNext()) {
                atributos.add(superClassPropertyIterator.next().getNodeName());
            }
            
            PersistentClass persistentClass = config.getClassMapping(
                    entidade.getClass().getSuperclass().getSuperclass().getCanonicalName());
            
            if(Util.preenchido(persistentClass)){
            	
            	superClassPropertyIterator = persistentClass.getPropertyIterator();

                while(superClassPropertyIterator.hasNext()) {
                    atributos.add(superClassPropertyIterator.next().getNodeName());
                }
            }
        }

        while(propertyIterator.hasNext()) {
            atributos.add(propertyIterator.next().getNodeName());
        }

        return atributos;
    }

    private static Configuration getHibernateConfiguration() {

        if(hbConfiguration == null) {
            Configuration config = new Configuration();
            config.configure("hibernate.cfg.xml");
            config.buildMappings();
            hbConfiguration = config;
        }

        return hbConfiguration;
    }

    /**
     * Um atributo � considerado direto se ele NAO � uma associacao.
     * 
     * @param entidade
     * @param nomeAtributo
     * @return
     */
    public static Boolean isAtributoDireto(Entidade entidade, String nomeAtributo) {

        Property atributo = getHibernateConfiguration().getClassMapping(entidade.getClass().getCanonicalName()).getProperty(nomeAtributo);

        return (atributo != null) && (!atributo.getType().isAssociationType());
    }

    public static Criteria criaFiltro(String alias, Entidade entidade, Criteria criteria, List<String> aliasCriados, MatchMode matchMode, JoinType tipoJoin) {
        List<String> atributosIndiretos = nomesAtributosIndiretos(entidade);
        Integer valorPk = entidade.getId();
		if(Util.preenchido(valorPk) && valorPk > 0) {
        	/* Caso o id esteja preenchido, *todos* os outros atributos devem ser ignorados, por isso ja retornamos
        	 * o criterio de busca. */
            String nomeCampo = "id";
            criteria.add(Restrictions.eq(geraAssociacao(alias, nomeCampo), valorPk));
            return criteria;
        } else {
            adicionaFiltroOutrosAtributos(alias, entidade, criteria, matchMode);
        }

        for(String nomeAtributo : atributosIndiretos) {
            Object valorAtributo = UtilReflect.retornarValorAtributo(entidade, nomeAtributo);

            if(isLista(valorAtributo) && !isEmpty(valorAtributo)) {
                valorAtributo = ((List<?>) valorAtributo).get(0);
            } else if(isLista(valorAtributo) && isEmpty(valorAtributo)) {
                continue;
            }

            if(Util.preenchido(valorAtributo) && aliasAindaNaoCriado(aliasCriados, valorAtributo)) {
                String proximoAlias = geraAlias(valorAtributo);
                criteria.createAlias(geraAssociacao(alias, nomeAtributo), proximoAlias, retornarTipoJoin(tipoJoin));
                /* Para nao criar esse alias novamente */
                aliasCriados.add(proximoAlias);
                Finder.criaFiltro(proximoAlias, (Entidade) valorAtributo, criteria, aliasCriados, matchMode, tipoJoin);
            }
        }

        return criteria;
    }

    private static int retornarTipoJoin(JoinType tipoJoin) {
        switch(tipoJoin) {
        case INNER_JOIN:
            return Criteria.INNER_JOIN;
        case LEFT_JOIN:
            return Criteria.LEFT_JOIN;
        case FULL_JOIN:
            return Criteria.FULL_JOIN;
        default:
            return Criteria.INNER_JOIN;
        }
    }

    private static boolean aliasAindaNaoCriado(List<String> aliasCriados, Object valorAtributo) {
        return !aliasCriados.contains(geraAlias(valorAtributo));
    }

    private static boolean isEmpty(Object valorAtributo) {
        return ((List<?>) valorAtributo).isEmpty();
    }

    private static boolean isLista(Object valorAtributo) {
        return valorAtributo instanceof List;
    }

    /**
     * Gera uma string de associacao entre um alias e seu atributo
     * 
     * @param alias
     * @param nomeAtributo
     * @return
     */

    public static String geraAssociacao(String alias, String nomeAtributo) {

        if(Util.preenchido(alias)) {
            return alias + "." + nomeAtributo;
        } else {
            return nomeAtributo;
        }

    }

    /**
     * Gera um Alias unico para um atributo qualquer.
     * 
     * @param valorAtributo
     * @return
     */
    public static String geraAlias(Object valorAtributo) {
        String proximoAlias = "";
        if(Util.preenchido(valorAtributo)) {
            proximoAlias = valorAtributo.getClass().getCanonicalName() + "_" + valorAtributo.hashCode();
        }
        return proximoAlias.replace(".", "_");
    }
    
    public static String geraAlias(Class<? extends Entidade> classe){
        String proximoAlias = "";
        if(Util.preenchido(classe)) {
            proximoAlias = classe.getCanonicalName();
        }
        return proximoAlias.replace(".", "_");
    }
    

    private static boolean chavePrimariaEstaPreenchida(int valorPk) {
    	boolean retorno = false ;
       if(Util.preenchido(valorPk)){
    	   if(valorPk > 0){
    		   retorno = true ;
    	   }
       }
       
       return retorno; 
    }

    private static void adicionaFiltroOutrosAtributos(String alias, Entidade entidade, Criteria criteria, MatchMode matchMode) {
        List<String> attributos = nomesAtributosDiretos(entidade);

        for(String nomeAtributo : attributos) {
            Object valorAtributo = UtilReflect.retornarValorAtributo(entidade, nomeAtributo);

            if(isAtributoDireto(entidade, nomeAtributo) && Util.preenchido(valorAtributo)) {
                if(isString(valorAtributo)) {
                    criteria.add(Restrictions.like(geraAssociacao(alias, nomeAtributo), (String) valorAtributo, matchMode));
                } else if(seletor.include(valorAtributo, nomeAtributo, null)) {
                    criteria.add(Restrictions.eq(geraAssociacao(alias, nomeAtributo), valorAtributo));
                }
            }
        }
    }

    private static boolean isString(Object valorAtributo) {
        return (valorAtributo instanceof String);
    }

    /**
     * Retorna uma lista com o nome de todos os atributos "indiretos", ou seja, todos os atributos que na verdade
     * refernciam uma Entidade inclusive os atributos tipo Lista (Bag, Set, List, etc)
     * 
     * @param entidade
     * @return
     */
    public static List<String> nomesAtributosIndiretos(Entidade entidade) {

        List<String> atributos = nomesAtributosMapeados(entidade);
        List<String> atributosIndiretos = new ArrayList<String>();

        for(String nomeAtributo : atributos) {
            if(!isAtributoDireto(entidade, nomeAtributo)) {
                atributosIndiretos.add(nomeAtributo);
            }
        }

        return atributosIndiretos;
    }

    public static List<String> nomesAtributosDiretos(Entidade entidade) {

        List<String> atributosDiretos = new ArrayList<String>();

        atributosDiretos = nomesAtributosMapeados(entidade);
        atributosDiretos.removeAll(nomesAtributosIndiretos(entidade));

        return atributosDiretos;
    }
    
    /** Esse m�todo converte uma lista de objetos em uma lista de ids.
     * 
     * OBS.: os objetos da lista devem extender Entidade**/
    public static <E extends Entidade> List<Integer> converterObjetosEmIds(List<E> objetos) {
    	
		List<Integer> ids = new ArrayList<Integer>();
		
		for(E objeto: objetos){
			ids.add(objeto.getId());
		}
		
		return ids;
	}
}
