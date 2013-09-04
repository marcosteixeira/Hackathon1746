package br.com.hackathon.services;

import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import br.com.hackathon.modelo.entidade.Usuario;
import br.com.hackathon.util.UtilFacebook;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoginService extends BaseService {
	private static final Logger log = Logger.getLogger(LoginService.class);

	public static void validarSignedRequest(String signedRequest) throws Exception {
		String[] split = signedRequest.split("[.]", 2);

	    String encoded_sig = split[0];
	    String encoded_envelope = split[1];
	    String jsonString = new String(base64_url_decode(encoded_envelope));
	    
	    JsonObject envelope = (JsonObject) new JsonParser().parse(jsonString);
	    String algorithm = envelope.get("algorithm").getAsString();

	    if (!algorithm.equals("HMAC-SHA256")) {
	      throw new Exception("Invalid request. (Unsupported algorithm.)");
	    }

	    if (((Long) envelope.get("issued_at").getAsLong()) < System.currentTimeMillis() / 1000 - 3600) {
	      throw new Exception("Invalid request. (Too old.)");
	    }

	    byte[] key = UtilFacebook.SECRET.getBytes();
	    SecretKey hmacKey = new SecretKeySpec(key, "HMACSHA256");
	    Mac mac = Mac.getInstance("HMACSHA256");
	    mac.init(hmacKey);
	    byte[] digest = mac.doFinal(encoded_envelope.getBytes());

	    if (!Arrays.equals(base64_url_decode(encoded_sig), digest)) {
	      throw new Exception("Invalid request. (Invalid signature.)");
	    }
		
	}

	private static byte[] base64_url_decode(String input) {
		return new Base64(true).decode(input);
	}

	public static void fecharSessoesAbertas() {
		// TODO Auto-generated method stub
		
	}

	public static void adicionaHistoricoLogin(Usuario usuario) {
		// TODO Auto-generated method stub
		
	}
}
