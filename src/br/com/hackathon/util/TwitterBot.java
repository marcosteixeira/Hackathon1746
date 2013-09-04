package br.com.hackathon.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import br.com.hackathon.hibernate.Buscador;
import br.com.hackathon.modelo.entidade.Endereco;
import br.com.hackathon.modelo.entidade.Ocorrencia;
import br.com.hackathon.modelo.entidade.Servico;
import br.com.hackathon.modelo.entidade.Usuario;
import br.com.hackathon.services.BaseService;

public class TwitterBot {

	public static TwitterStream twitterStream;
	public static ConfigurationBuilder cb;
	public static HashMap<String, Usuario> busyList = new HashMap<String,Usuario>();
	
	private static final String hashtag = "#Alo1746";
	private static final String CONSUMER_KEY = "FtwRECuRXPic6ofVPMphoA";
	private static final String CONSUMER_SECRET = "tqBFcphw4tBWIXzWDNzVd8Lydc4n6G9pjJUnkEgKNM";
	private static final String ACCESS_TOKEN = "1717908626-PU6YXJMxvGtdUEHE7SBbv6YBZfQo8BamlA6a4y2";
	private static final String ACCESS_TOKEN_SECRET = "CbdtCwTn4C0ocpfHUfPRicgJlpfRbPRjoX0yJYGi38";
	

	public  static void monitorarTwitter(Logger log){
		
		log.info("Iniciando monitoramento do Twitter...");
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(CONSUMER_KEY)
		  .setOAuthConsumerSecret(CONSUMER_SECRET)
		  .setOAuthAccessToken(ACCESS_TOKEN)
		  .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

		twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

		StatusListener listener = new StatusListener() {
			
		    public void onStatus(Status status) {
		    	criarPreOcorrencia(status);
		        
		        if(Util.preenchido(status.getGeoLocation()) && Util.preenchido(status.getGeoLocation().getLatitude())){
		        	System.out.println(status.getGeoLocation().getLatitude());
		        }else{
		        	System.out.println("Sem Location");
		        }
		        if(Util.preenchido(status.getPlace()) && Util.preenchido(status.getPlace().getName())){
		        	System.out.println(status.getPlace().getName());
		        }else{
		        	System.out.println("Sem Place");
		        }
		    }

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		        System.out.println("Post Deletado:" + statusDeletionNotice.getStatusId());
		    }
		    
		    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		        System.err.println("Limitação:" + numberOfLimitedStatuses);
		    }
		    
		    public void onScrubGeo(long userId, long upToStatusId) {
		        System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
		    }
		    
		    public void onStallWarning(StallWarning warning) {
		        System.out.println("Got stall warning:" + warning);
		    }
		    
		    public void onException(Exception ex) {
		        ex.printStackTrace();
		    }
		};

		List<String> queries = new ArrayList<String>();
		queries.add(hashtag);

		twitterStream.addListener(listener);
		

		String[] trackQueries = (String[]) queries.toArray(new String[queries.size()]);

		FilterQuery filterQuery = new FilterQuery();
		twitterStream.filter(filterQuery.track(trackQueries));
		log.info("Monitoramento iniciado! Hashtag: " + hashtag);
	}
	
	public static void criarPreOcorrencia(Status status){
		String tweet = retirarHashtag(status).toLowerCase();
		tweet = retirarConjuncoes(tweet);
		String tipo = retornarTipo(tweet);
		tweet = retirarTipo(tweet);
		if(Util.vazio(tipo)){
			return;
		}else{
			tweet = tweet.replace(tipo, "");
			Endereco endereco = new Endereco();
			endereco.setLogradouro(tweet);
			
			Servico servico = new Servico();
			servico.setNome(tipo);
			servico = new Buscador().selecionar(servico);
			
			Ocorrencia ocorrencia = new Ocorrencia();
			ocorrencia.setServico(servico);
			ocorrencia.setEndereco(endereco);
			ocorrencia.setStatus("0");
			ocorrencia.setDataCriacao(UtilData.agora());
			
			Usuario usuario = new Usuario();
			usuario.setIdTwitter(status.getUser().getScreenName());
			usuario = new Buscador().selecionar(usuario);
			if(Util.vazio(usuario)){
				usuario = new Usuario();
				usuario.setIdTwitter(status.getUser().getScreenName());
				BaseService.salvar(usuario);
			}

			BaseService.salvar(endereco);
			
			ocorrencia.setUsuario(usuario);
			BaseService.salvar(ocorrencia);
			
			String linkPreOcorrencia = UtilNumero.proximaString();
			
			while(busyList.containsKey(linkPreOcorrencia)){
				linkPreOcorrencia = UtilNumero.proximaString();
			}
			
			busyList.put(linkPreOcorrencia, usuario);
			
			tweetarNovoLink(linkPreOcorrencia);
		}
	}

	private static String retirarTipo(String tweet) {
		String nome = null;
		if(tweet.contains("buraco via")){
			nome = tweet.replace("buraco via", "");
		}else if(tweet.contains("buraco")){
			nome = tweet.replace("buraco", "");
		}else if(tweet.contains("estacionamento irregular")){
			nome = tweet.replace("estacionamento irregular", "");
		}else if(tweet.contains("carro estacionado calçada")){
			nome = tweet.replace("carro estacionado calçada", "");
		}else if(tweet.contains("carro estacionado")){
			nome = tweet.replace("carro estacionado", "");
		}else if(tweet.contains("podar árvore")){
			nome = tweet.replace("podar árvore", "");
		}else if(tweet.contains("poste sem luz")){
			nome = tweet.replace("poste sem luz", "");
		}else if(tweet.contains("rua sem iluminação")){
			nome = tweet.replace("rua sem iluminação", "");
		}else if(tweet.contains("lâmpada queimada")){
			nome = tweet.replace("lâmpada queimada", "");
		}
		return nome;
	}

	private static void tweetarNovoLink(String linkPreOcorrencia) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(CONSUMER_KEY)
		  .setOAuthConsumerSecret(CONSUMER_SECRET)
		  .setOAuthAccessToken(ACCESS_TOKEN)
		  .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
		
		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		try {
			twitter.updateStatus("@" + busyList.get(linkPreOcorrencia).getIdTwitter() + " sua ocorrência está quase pronta! Por favor, acesse aqui para finalizá-la: " + UtilContexto.getBaseUrl() +"/tt/"+ linkPreOcorrencia);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static String retirarConjuncoes(String tweet) {
		tweet = tweet.replace(" na ", "");
		tweet = tweet.replace(" em ", "");
		
		return tweet;
	}

	private static String retornarTipo(String tweet) {
		String nome = null;
		if(tweet.contains("buraco via")){
			nome = "Conservação de vias";
		}else if(tweet.contains("buraco")){
			nome = "Conservação de vias";
		}else if(tweet.contains("estacionamento irregular")){
			nome = "Estacionamento irregular";
		}else if(tweet.contains("carro estacionado calçada")){
			nome = "Estacionamento irregular";
		}else if(tweet.contains("carro estacionado")){
			nome = "Estacionamento irregular";
		}else if(tweet.contains("podar árvore")){
			nome = "Poda de árvores";
		}else if(tweet.contains("poste sem luz")){
			nome = "Iluminação pública";
		}else if(tweet.contains("rua sem iluminação")){
			nome = "Iluminação pública";
		}else if(tweet.contains("lâmpada queimada")){
			nome = "Iluminação pública";
		}
		return nome;
	}

	private static String retirarHashtag(Status status) {
		String tweet = status.getText().replace(hashtag, "");
		return tweet; 
	}
	
}
