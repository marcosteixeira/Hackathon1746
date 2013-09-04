package br.com.hackathon.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import br.com.hackathon.services.LoginService;
import br.com.hackathon.util.TwitterBot;
import br.com.hackathon.util.UtilContexto;

public class ContextListener implements ServletContextListener{

	Logger log = Logger.getLogger(ContextListener.class);
	
	public void contextDestroyed(ServletContextEvent arg0) {
		LoginService.fecharSessoesAbertas();
		
	}

	public void contextInitialized(ServletContextEvent contextEvent) {
		String fullPath = contextEvent.getServletContext().getRealPath("");

		String contexto = obtemNomeDoContexto(fullPath);

		log.info("Inicializando contexto: " + contextEvent.getServletContext().getServletContextName() + " [" + contexto + "]");
		
		LoginService.fecharSessoesAbertas();

		UtilContexto.setNomeContexto(contexto);
		UtilContexto.setCaminhoCompletoNoDisco(fullPath);
		
		TwitterBot.monitorarTwitter(log);
	}

	/**
	 * 
	 * Retorna o nome do contexto dessa aplicação que está _startando_. O
	 * retorno é no formato "/" + nomeContexto
	 * 
	 * O nome é extraído do caminho completo no disco, onde está o _war_.
	 * Funciona tanto para caminhos separados por "/" como para caminhos
	 * separados po "\"
	 * 
	 * @param fullPath
	 * @return
	 */
	public static String obtemNomeDoContexto(String fullPath) {

		String contexto = "";

		if (fullPath.lastIndexOf("\\") > 0) {
			contexto = fullPath.substring(fullPath.lastIndexOf("\\") + 1);
		}

		if (fullPath.lastIndexOf("/") > 0) {
			contexto = fullPath.substring(fullPath.lastIndexOf("/") + 1);
		}

		return "/" + contexto;

	}

	
}
