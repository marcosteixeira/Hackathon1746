package br.com.hackathon.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;



public class UtilFacebook {

	private static final String ENDERECO_GRAPH = "https://graph.facebook.com/";
	@SuppressWarnings("unused")
	private static final String SUFIXO_NOTIFICATIONS = "/notifications";
	private static final String HTTPS_GRAPH_FACEBOOK_COM_ME_ACCESS_TOKEN = ENDERECO_GRAPH + "me?access_token=";
	public static final String SECRET = "null por enquanto";
	
	
	public static String getJsonUsuario(String accessToken) throws Exception{
		URL acessoToken = new URL(HTTPS_GRAPH_FACEBOOK_COM_ME_ACCESS_TOKEN + accessToken);
		return getContentResult(acessoToken);
		
	}
	
	public static String getContentResult(URL urlResponse) throws Exception {
		URLConnection connection = urlResponse.openConnection();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
						connection.getInputStream()));
		
		StringBuilder response = new StringBuilder();
		String inputLine;
		
		while ((inputLine = in.readLine()) != null) 
			response.append(inputLine);
		
		in.close();
		
		return response.toString();
	}
}
