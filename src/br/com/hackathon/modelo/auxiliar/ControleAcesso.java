package br.com.hackathon.modelo.auxiliar;

import java.io.Serializable;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import br.com.hackathon.sessao.SessaoGeral;
import br.com.hackathon.util.Util;

/**
 * Classe usada para encerrar a sessão no banco. 
 * Não está sendo usada para login simultaneo
 * @author marcosteixeira
 *
 */
public class ControleAcesso implements Serializable, HttpSessionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1634194307820826829L;

	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void sessionDestroyed(HttpSessionEvent event) {
	}

}
