package br.com.hackathon.sessao;

import java.io.Serializable;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;
import br.com.hackathon.modelo.entidade.Ocorrencia;
import br.com.hackathon.modelo.entidade.Usuario;

@Component
@SessionScoped
public class SessaoGeral implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3480034946212173761L;
	
	private Usuario usuario;
	private Ocorrencia ocorrencia;
	private String userAgent;
	private Twitter twitter;
	private RequestToken requestToken;


	public RequestToken getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getUserAgent() {
		return userAgent;
	}
	
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public boolean isLogged() {
		return usuario != null;
	}
	
	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}
}
