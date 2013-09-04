package br.com.hackathon.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

public class GeradorDeMd5 {
	public static String converter(String string) {

		MessageDigest m;

		try {

			m = MessageDigest.getInstance("MD5");
			m.update(string.getBytes(), 0, string.length());
			return new BigInteger(1, m.digest()).toString(16);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getStringAleatoria() {
		String letras = "ABCDEFGHIJKLMNOPQRSTUVYWXZabcdefghijklmnopqrstuvywxz";  
		
		Random random = new Random();  
		
		String armazenaChaves = "";  
		int index = -1;  
		for( int i = 0; i < 9; i++ ) {  
			index = random.nextInt( letras.length() );  
			armazenaChaves += letras.substring( index, index + 1 );  
		}  
		
		return converter(armazenaChaves);
	}


}
