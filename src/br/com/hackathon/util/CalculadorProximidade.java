package br.com.hackathon.util;

import java.util.List;

import br.com.hackathon.hibernate.Buscador;
import br.com.hackathon.modelo.entidade.Ocorrencia;

public class CalculadorProximidade {

	private static double earthRadius = 6371.01;
	public static double DISTANCIA_1 = 1000;
	public static double DISTANCIA_2 = 5000;
	
	public static List<Ocorrencia> encontrarLugaresDistancia(double latitude, double longitude, double distancia){
		GeoLocation lugar = GeoLocation.fromDegrees(latitude, longitude);
				
		GeoLocation[] boundingCoordinates =	lugar.boundingCoordinates(distancia, earthRadius);
		//boolean meridian180WithinDistance = boundingCoordinates[0].getLongitudeInRadians() > boundingCoordinates[1].getLongitudeInRadians();
		
		double r = distancia/earthRadius;
		//double latitudeT = Math.asin(Math.sin(latitude)/Math.cos(r));
		double latitudeMin = latitude - r;
		double latitudeMax = latitude + r;
		double longitudeMin = longitude - Math.asin(Math.sin(r)/Math.cos(latitude));
		double longitudeMax = longitude + Math.asin(Math.sin(r)/Math.cos(latitude));
		
		@SuppressWarnings("unchecked")
		List<Ocorrencia> ocorrencias = new Buscador().buscaPorHql("SELECT * FROM Endereco WHERE "
				+ "(latitude >= "+ latitudeMin +" AND latitude <= "+latitudeMax+") AND "
						+ "(longitude >= "+ longitudeMin + " AND longitude <= " + longitudeMax +") "
								+ "AND acos(sin("+latitude+") * sin(latitude) + cos("+latitude+") * cos(latitude)"
										+ " * cos(longitude - ("+longitude+"))) <= "+r);
		
		return ocorrencias;
	}
	
}
