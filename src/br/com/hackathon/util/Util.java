package br.com.hackathon.util;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public class Util {

	public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

	public static boolean vazio(Object objeto) {

		if (objeto == null) {
			return true;
		}

		if (objeto instanceof String) {
			if (((String) objeto).trim().equals("")) {
				return true;
			} else {
				return false;
			}
		}

		if (objeto instanceof Collection) {
			if (((Collection<?>) objeto).isEmpty()) {
				return true;
			} else {
				return false;
			}
		}

		if (objeto instanceof Map) {
			if (((Map<?, ?>) objeto).isEmpty()) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	public static boolean preenchido(Object objeto) {
		return !vazio(objeto);
	}

	public static boolean verdadeiro(Boolean booleano) {
		if(Util.preenchido(booleano)){
			return booleano;
		}else{
			return false;
		}
	}
}
