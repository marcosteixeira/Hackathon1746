package br.com.hackathon.util;

import br.com.caelum.vraptor.Result;

public class UtilResult {

	private static Result result;

	public static Result getResult() {
		return result;
	}

	public static void setResult(Result result) {
		UtilResult.result = result;
	}

}