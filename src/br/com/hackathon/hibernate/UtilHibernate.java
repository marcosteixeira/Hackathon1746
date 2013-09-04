package br.com.hackathon.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

public class UtilHibernate {
	
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {

		    Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.buildMappings();
            ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(configuration.getProperties());
            

			sessionFactory = configuration.buildSessionFactory(serviceRegistryBuilder.buildServiceRegistry());
		}
		return sessionFactory;
	}
	
    public static String geraAlias(Criteria criteria, String nomeAtributo, List<String> aliasCriados) {

        int indice = 0;
        String atributoAlias;
        String nomeAlias;

        indice = nomeAtributo.indexOf(".");

        //pega o nome do atributo até a primeira ocorrência de ponto (.)
        atributoAlias = nomeAtributo.substring(0, indice);

        nomeAlias = gerarNomeAlias(nomeAtributo);

        if(!aliasCriados.contains(nomeAlias)) {
            criteria.createAlias(atributoAlias, nomeAlias);

            aliasCriados.add(nomeAlias);
        }

        return nomeAlias + nomeAtributo.substring(indice);
    }
    
    public static String gerarNomeAlias(String nomeAtributo) {

        String nomeAlias;
        //pega a primeira letra
        nomeAlias = nomeAtributo.substring(0, 1);

        for(char caracter : nomeAtributo.toCharArray()) {
            if(Character.isUpperCase(caracter)) {
                nomeAlias += Character.toLowerCase(caracter);
            }
        }
        return nomeAlias;
    }
}
