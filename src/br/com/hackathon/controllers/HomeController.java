package br.com.hackathon.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.hackathon.hibernate.Buscador;
import br.com.hackathon.modelo.entidade.Ocorrencia;
import br.com.hackathon.modelo.entidade.Usuario;
import br.com.hackathon.sessao.SessaoGeral;
import br.com.hackathon.util.TwitterBot;
import br.com.hackathon.util.Util;

@Resource
public class HomeController {
	
	private final Result result;
	private Validator validator;
	private SessaoGeral sessaoGeral;
	
	public HomeController(Result result, Validator validator, SessaoGeral sessaoGeral){
		this.result = result;
		this.validator = validator;
		this.sessaoGeral = sessaoGeral;
	}
	
	@Path("/")
	public void home() {
		new Buscador().listar2(new Ocorrencia());
		sessaoGeral.setOcorrencia(new Ocorrencia());
	}
	
	public void logarTwitter(HttpServletRequest request, HttpServletResponse response){
				
		String CONSUMER_KEY = "FtwRECuRXPic6ofVPMphoA";
		String CONSUMER_SECRET = "tqBFcphw4tBWIXzWDNzVd8Lydc4n6G9pjJUnkEgKNM";
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(CONSUMER_KEY)
		  .setOAuthConsumerSecret(CONSUMER_SECRET);
		
		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		sessaoGeral.setTwitter(twitter);
        try {
            String callbackURL = "http://localhost:8080/hackathon/home/confirmarLoginTwitter";

            RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL);
            sessaoGeral.setRequestToken(requestToken);
            //response.sendRedirect(requestToken.getAuthenticationURL());
            result.redirectTo(requestToken.getAuthenticationURL());
            return;
        } catch (TwitterException e) {
            try {
				throw new ServletException(e);
			} catch (ServletException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
	}
	
	public void confirmarLoginTwitter(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		Twitter twitter = sessaoGeral.getTwitter();
        RequestToken requestToken = sessaoGeral.getRequestToken();
        String verifier = request.getParameter("oauth_verifier");
        try {
            twitter.getOAuthAccessToken(requestToken, verifier);          
            if(!sessaoGeral.getUsuario().getIdTwitter().equals(twitter.getScreenName())){
            	sessaoGeral.setOcorrencia(null);
            	sessaoGeral.setUsuario(null);
            	return;
            }else{
            	request.getSession().removeAttribute("requestToken");
            	result.redirectTo(OcorrenciaController.class).criarOcorrencia(sessaoGeral.getOcorrencia());
            	return;
            }
        } catch (TwitterException e) {
            result.redirectTo(this).home();
            throw new ServletException(e);            
        }

	}
}
