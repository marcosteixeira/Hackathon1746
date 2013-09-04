package br.com.hackathon.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;



public class TemplateTransacao {

	private static SessionFactory sessionFactory;
	
	public static Object executa(ATransacao transacao) {
		
		Session sessaoAberta = obtemSessaoAberta();

		Transaction transacaoHibernate = sessaoAberta.beginTransaction();

		Object resultadoTransacao = null;
		
		try{
			resultadoTransacao = transacao.executaTransacao(sessaoAberta);

			if (transacao.precisaDeCommit()){
				transacaoHibernate.commit();
			}
			
		}catch (RuntimeException e) {
			transacaoHibernate.rollback();
			throw e;
		}finally{
			sessaoAberta.close();
		}
		
		return resultadoTransacao;
		
	}

	private static Session obtemSessaoAberta() {
		return obtemSessionFactory().openSession();
	}

	private static SessionFactory obtemSessionFactory() {
		
		if(sessionFactory == null) {
			sessionFactory = UtilHibernate.getSessionFactory(); 
		}
		
		return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		TemplateTransacao.sessionFactory = sessionFactory;
	}

}
