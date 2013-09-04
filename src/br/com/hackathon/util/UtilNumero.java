package br.com.hackathon.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

public class UtilNumero {

	public static final String ZERO = "0";
	public final static int PRECISAO_PADRAO_CALCULO = 30;
	public final static int METODO_ARREDONDAMENTO_PADRAO = BigDecimal.ROUND_HALF_EVEN;
	public final static int METODO_ARREDONDAMENTO_ROUND_DOWN = BigDecimal.ROUND_DOWN;
	public final static int SEM_ARREDONDAMENTO = BigDecimal.ROUND_UNNECESSARY;

	private static SecureRandom random = new SecureRandom();

	/*******************************************************************************************************************
	 * ******************************************** MET�DOS DE VALIDA��O
	 * ********************************************* *
	 ******************************************************************************************************************/

	/**
	 * Verifica se uma String do tipo int.
	 * 
	 * @param intNum
	 *            - String contendo o valor a ser verificado
	 * @return <code>true</code> se for int, se n�o, retorna <code>false</code>
	 */

	public static boolean isInt(Object intNum) {

		try {
			Integer.parseInt(String.valueOf(intNum));
			return true;

		} catch (RuntimeException e) {
			// para NumberFormatException e NullPointerException
			return false;
		}
	}

	/**
	 * Verifica se o n�mero � um n�mero decimal v�lido para o BigDecimal
	 * 
	 * @param decimalNum
	 * @return
	 */
	public static boolean isBigDecimal(Object decimalNum) {

		try {
			new BigDecimal(String.valueOf(decimalNum));
			return true;

		} catch (RuntimeException e) {
			// para NumberFormatException e NullPointerException
			return false;
		}
	}

	/**
	 * Verifica se a string � um inteiro positivo (maior que 0)
	 * 
	 * @param intNum
	 *            String com o n�mero
	 * @return Retorna "true" caso seja um inteiro positivo (maior que 0) e
	 *         "false" caso contr�rio
	 */
	public static boolean isIntPositivo(Object intNum) {

		int numero;

		if (isInt(intNum)) {
			numero = Integer.parseInt(String.valueOf(intNum));

			if (numero > 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Verifica se a string � um inteiro n�o negativo (maior ou igual a 0))
	 * 
	 * @param intNum
	 *            string com o n�mero
	 * @return Retorna "true" caso seja um inteiro n�o negativo (maior ou igual
	 *         a 0) e "false" caso contr�rio
	 */
	public static boolean isIntNaoNegativo(String intNum) {

		int numero = 0;

		try {
			numero = Integer.parseInt(intNum);

			if (numero >= 0) {
				return true;
			} else {
				return false;
			}

		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Verifica se um n�mero digitado � um n�mero monet�rio. Um n�mero monet�rio
	 * deve aceitar o uso de pontos (".") para separar milhares e v�rgula (",")
	 * para separar casas decimais. Podemos usar uma, duas ou nenhuma casa
	 * decimal.
	 * 
	 * @param numero
	 *            N�mero que ser� verificado
	 * @return "True" caso seja um n�mero monet�rio e "false" caso contr�rio
	 */
	public static boolean isNumeroMonetario(String numero) {
		return UtilString.verificarFormato(numero, "^\\d{1,3}(\\.?\\d{3})*(,\\d{1,2})?$");
	}

	/**
	 * Verifica se um n�mero digitado � fracion�rio. Um n�mero fracion�rio deve
	 * aceitar o uso de pontos (".") para separar milhares e v�rgula (",") para
	 * separar casas decimais. Podemos usar de uma a seis casas decimais, al�m
	 * da op��o de n�o usar.
	 * 
	 * @param numero
	 *            N�mero que ser� verificado
	 * @return "True" caso seja um n�mero fracion�rio e "false" caso contr�rio
	 */
	public static boolean isNumero(String numero) {
		return UtilString.verificarFormato(numero, "^(\\-)?(([1-9]{1}\\d{0,2})(\\.?\\d{3})*|0+)(,?\\d+)?$");
	}

	/**
	 * Verifica se um n�mero digitado � fracion�rio positivo (ou seja, n�o
	 * inclui zero). Um n�mero fracion�rio deve aceitar o uso de pontos (".")
	 * para separar milhares e v�rgula (",") para separar casas decimais.
	 * Podemos usar de uma a seis casas decimais, al�m da op��o de n�o usar.
	 * 
	 * @param numero
	 *            N�mero que ser� verificado
	 * @return "True" caso seja um n�mero fracion�rio positivo e "false" caso
	 *         contr�rio
	 */
	public static boolean isNumeroFracionarioPositivo(String numero) {

		numero = UtilString.retirarSimbolos(numero);

		return UtilString.verificarFormato(numero, "^[0-9]{1}[0-9]{0,3}(\\.?\\d{3})*(,\\d{1,6})?$") && !ZERO.equals(numero);
	}

	/**
	 * Verifica se um n�mero digitado � fracion�rio n�o negativo (ou seja,
	 * inclui o zero). Um n�mero fracion�rio deve aceitar o uso de pontos (".")
	 * para separar milhares e v�rgula (",") para separar casas decimais.
	 * Podemos usar de uma a seis casas decimais, al�m da op��o de n�o usar.
	 * 
	 * @param numero
	 *            N�mero que ser� verificado
	 * @return "True" caso seja um n�mero fracion�rio n�o negativo e "false"
	 *         caso contr�rio
	 */
	public static boolean isNumeroFracionarioNaoNegativo(String numero) {

		if (!UtilString.apareceNoMaximoUmaVez(numero, ",")) {
			return false;
		}

		numero = UtilString.retirarSimbolos(numero);
		return UtilString.verificarFormato(numero, "^[0-9]{1}[0-9]{0,3}(\\.?\\d{3})*(,\\d{1,6})?$") || ZERO.equals(numero);
	}

	/**
	 * Verifica se a string passada � um n�mero e que tem no m�ximo uma v�rgula
	 */
	public static boolean isNumber(String numero) {

		if (!UtilString.apareceNoMaximoUmaVez(numero, ",")) {
			return false;
		}

		numero = UtilString.retirarSimbolos(numero);
		return UtilString.verificarFormato(numero, "^-?[0-9]{1}[0-9]{0,3}(\\.?\\d{3})*(,\\d{1,6})?$") || ZERO.equals(numero);
	}

	/**
	 * Verifica se um n�mero digitado � inteiro. Um n�mero inteiro deve aceitar
	 * o uso de pontos (".") para separar milhares e n�o deve permitir o uso de
	 * v�rgula (",").
	 * 
	 * @param numero
	 *            N�mero que ser� verificado
	 * @return "True" caso seja um n�mero inteiro e "false" caso contr�rio
	 */
	public static boolean isNumeroInteiro(String numero) {
		return UtilString.verificarFormato(numero, "^\\d{1,3}(\\.?\\d{3})*$");
	}

	/*******************************************************************************************************************
	 * ******************************************** MET�DOS DE FORMATA��O
	 * ******************************************** *
	 ******************************************************************************************************************/

	public static String formatarNumeroComDuasCasasDecimaisComPonto(Number number) {
		return UtilNumero.formatarNumero(number, true, 2);
	}

	public static String formatarNumeroSemCasasDecimaisComPonto(Number number) {
		return UtilNumero.formatarNumero(number, true, 0);
	}

	/**
	 * @deprecated Usar formatarNumeroComDuasCasasDecimaisComPonto(Number), mas
	 *             � necess�rio cuidado para garantir que n�o haver� problema
	 *             com a aceita��o do ponto
	 */
	public static String formatarNumeroComDuasCasasDecimaisSemPonto(Number number) {
		return UtilNumero.formatarNumero(number, false, 2);
	}

	/**
	 * Transforma um n�mero em String
	 * 
	 * @param number
	 *            N�mero que ser� transformado em String
	 * @param comPonto
	 *            Indica se o n�mero ser� exibido com ponto para separa��o de
	 *            milhares
	 * @param qtdeCasasDecimais
	 *            Indica o n�mero de casas decimais que ser�o exibidas. Caso
	 *            seja 0 (zero), n�o mostra casas decimais
	 * @return Uma String com o n�mero na formata��o desejada
	 */
	public static String formatarNumero(final Number number, final boolean comPonto, final int qtdeCasasDecimais) {

		final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Util.LOCALE_PT_BR);
		final NumberFormat numberFormat;
		final String partePonto = "#,##";
		final String zero = ZERO;
		final String virgula = ".";
		String padrao = "";

		// se tiver ponto, adiciona a parte do ponto
		if (comPonto) {
			padrao = partePonto;
		}

		// sempre tem "0"
		padrao += zero;

		// se tiver casas decimais...
		if (qtdeCasasDecimais != 0) {

			// ... adiciona uma v�rgula (para o Java, um ".")...
			padrao += virgula;

			// ... e um "0" para cada casa decimal
			for (int i = 0; i < qtdeCasasDecimais; i++) {
				padrao += zero;
			}
		}

		numberFormat = new DecimalFormat(padrao, dfs);

		return numberFormat.format(number);
	}
	
	public static BigDecimal formatarNumeroBigDecimal(final Number number, final boolean comPonto, final int qtdeCasasDecimais) {

		return UtilNumero.formatarBigDecimal(formatarNumero(number, comPonto, qtdeCasasDecimais));
	}

	/**
	 * Transforma um n�mero em String. Os milhares s�o separados por "."
	 * (ponto), a parte decimal � separada por v�rgula e � exibido apenas o
	 * n�mero de casas decimais necess�rio.
	 * 
	 * @param number
	 *            N�mero que ser� transformado em String.
	 * @return Uma string representando um n�mero.
	 */
	public static String formatarNumero(final Number number) {

		final String resultado;
		final int qtdeCasasDecimais = calcularQtdeCasasDecimais(number);

		// formata o n�mero separando milharas com "." (ponto) e com a
		// quantidade de casas decimais necess�rias
		resultado = formatarNumero(number, true, qtdeCasasDecimais);

		return resultado;
	}
	
	
	/**
	 * Transforma um n�mero em String. Os milhares s�o separados por "."
	 * (ponto), a parte decimal � separada por v�rgula e � exibido apenas o
	 * n�mero de casas decimais necess�rio.
	 * 
	 * @param number
	 *            N�mero que ser� transformado em String.
	 * @return Uma string representando um n�mero.
	 */
	public static String formatarNumeroRetornandoNull(final Number number) {
		
		if(Util.vazio(number)) {
			return null;
		}
		
		final String resultado;
		final int qtdeCasasDecimais = calcularQtdeCasasDecimais(number);
		
		// formata o n�mero separando milharas com "." (ponto) e com a
		// quantidade de casas decimais necess�rias
		resultado = formatarNumero(number, true, qtdeCasasDecimais);
		
		return resultado;
	}
	
	/**
	 * Transforma um n�mero em String. Os milhares s�o separados por "."
	 * (ponto), a parte decimal � separada por v�rgula e � exibido apenas o
	 * n�mero de casas decimais necess�rio.
	 * 
	 * @param number
	 *            N�mero que ser� transformado em String.
	 * @return Uma string representando um n�mero.
	 */
	public static String formatarNumero(final Number number, Integer qtdeCasasDecimais) {
		
		final String resultado;
		
		if(Util.vazio(qtdeCasasDecimais)) {
			qtdeCasasDecimais = calcularQtdeCasasDecimais(number);
		}	
		
		// formata o n�mero separando milharas com "." (ponto) e com a
		// quantidade de casas decimais necess�rias
		resultado = formatarNumero(number, true, qtdeCasasDecimais);
		
		return resultado;
	}

	/**
	 * Transforma o n�mero em String sem usar ponto para milhar
	 * 
	 * @param number
	 * @return
	 */
	public static String formatarNumeroSemPonto(final Number number) {

		String resultado = null;

		if (Util.preenchido(number)) {

			final int qtdeCasasDecimais = calcularQtdeCasasDecimais(number);

			resultado = formatarNumero(number, false, qtdeCasasDecimais);
		}

		return resultado;
	}

	/**
	 * Calcula a qtde de casas decimais que o n�mero possui
	 * 
	 * @param number
	 * @return
	 */
	private static int calcularQtdeCasasDecimais(final Number number) {

		final int qtdeCasasDecimais; // quantidade de casas decimais do n�mero
		final String[] splitNumero = String.valueOf(number).split("\\."); // cria
																			// um
																			// String[2]
																			// que
																			// separa
																			// a
																			// parte
		// inteira da parte decimal

		// se tiver parte decimal
		if (splitNumero != null && splitNumero.length == 2) {
			// pega a parte decimal do n�mero e retira os zeros � direita, ou
			// seja, os desnecess�rios
			qtdeCasasDecimais = UtilString.retiraZeroDireita(splitNumero[1]).length();
		} else {
			// se n�o tiver parte decimal a quantidade � zero.
			qtdeCasasDecimais = 0;
		}
		return qtdeCasasDecimais;
	}
	
	/**
	 * Calcula a qtde de casas decimais que o n�mero possui considerando os zeros a direita
	 * 
	 * @param number
	 * @return
	 */
	public static int calcularQtdeCasasDecimaisComZeroDireita(final Number number) {
		
		if(Util.preenchido(number)) {
			final int qtdeCasasDecimais; // quantidade de casas decimais do n�mero
			final String[] splitNumero = String.valueOf(number).split("\\."); // cria
			// um
			// String[2]
			// que
			// separa
			// a
			// parte
			// inteira da parte decimal
			
			// se tiver parte decimal
			if (splitNumero != null && splitNumero.length == 2) {
				// pega a parte decimal do n�mero e retira os zeros � direita, ou
				// seja, os desnecess�rios
				qtdeCasasDecimais = splitNumero[1].length();
			} else {
				// se n�o tiver parte decimal a quantidade � zero.
				qtdeCasasDecimais = 0;
			}
			return qtdeCasasDecimais;
		} else {
			return 0;
		}
	}

	/**
	 * Faz valida��es comuns para m�todos que convertem uma String em um tipo de
	 * dado num�rico
	 * 
	 * @param stringNumero
	 *            String que ser� validada
	 * @throws NumberFormatException
	 *             Caso haja algum problema de valida��o
	 */
	public static String formatarNumeroString(final String stringNumero) throws NumberFormatException {

		final String stringFormatada;

		if (Util.vazio(stringNumero)) {
			throw new NumberFormatException("String n�o preenchida");
		}

		// tira todos os pontos da String
		stringFormatada = stringNumero.replace(".", "");

		// converte a virgula em ponto
		if (!UtilString.apareceNoMaximoUmaVez(stringFormatada, ",")) {
			throw new NumberFormatException("H� mais de uma v�rgula na string");
		}

		return stringFormatada.replace(",", ".");
	}

	public static float formatarFloat(final String stringNumero) throws NumberFormatException {

		final String stringFormatada = formatarNumeroString(stringNumero);
		final float resultado = Float.parseFloat(stringFormatada);

		if (resultado < 0) {
			throw new NumberFormatException("O n�mero n�o pode ser negativo");
		}

		return resultado;
	}

	public static double formatarDouble(final String stringNumero) throws NumberFormatException {

		final String stringFormatada = formatarNumeroString(stringNumero);
		final double resultado = Double.parseDouble(stringFormatada);

		if (resultado < 0) {
			throw new NumberFormatException("O n�mero n�o pode ser negativo");
		}

		return resultado;
	}

	public static BigDecimal formatarBigDecimal(final String stringNumero) throws NumberFormatException {
		return new BigDecimal(formatarNumeroString(stringNumero));
	}

	public static boolean menor(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2) {

		if (bigDecimal1.compareTo(bigDecimal2) == -1) {
			return true;
		}

		return false;
	}

	public static boolean maior(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2) {

		if (bigDecimal1.compareTo(bigDecimal2) == 1) {
			return true;
		}

		return false;
	}

	public static boolean igual(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2) {

		if (bigDecimal1.compareTo(bigDecimal2) == 0) {
			return true;
		}

		return false;
	}

	public static boolean diferente(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2) {

		if (bigDecimal1.compareTo(bigDecimal2) != 0) {
			return true;
		}

		return false;
	}

	/**
	 * Compara se "a" � maior ou igual a "b"
	 * 
	 * @param a
	 *            primeiro n�mero
	 * @param b
	 *            segundo n�mero
	 * @return true se "a" for maior ou igual a "b", e false caso contr�rio
	 */
	public static boolean maiorIgual(final BigDecimal a, final BigDecimal b) {
		if (a.compareTo(b) >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * Compara se "a" � menor ou igual a "b"
	 * 
	 * @param a
	 *            primeiro n�mero
	 * @param b
	 *            segundo n�mero
	 * @return true se "a" for menor ou igual a "b", e false caso contr�rio
	 */
	public static boolean menorIgual(final BigDecimal a, final BigDecimal b) {
		if (a.compareTo(b) <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * Retorna a soma de dois objetos BigDecimal
	 * 
	 * @param bigDecimal1
	 * @param bigDecimal2
	 * @return bigDecimal1 + bigDecimal2
	 */
	public static BigDecimal soma(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2) {
		return bigDecimal1.add(bigDecimal2);
	}

	/**
	 * Retorna a soma de objetos BigDecimal
	 * 
	 * @param numeros
	 * @return
	 */
	public static BigDecimal soma(final BigDecimal... numeros) {

		BigDecimal soma = BigDecimal.ZERO;

		for (BigDecimal bigDecimal : numeros) {
			soma = soma(bigDecimal, soma);
		}

		return soma;
	}

	/**
	 * Retorna a subtra��o de dois objetos BigDecimal
	 * 
	 * @param bigDecimal1
	 * @param bigDecimal2
	 * @return bigDecimal1 - bigDecimal2
	 */
	public static BigDecimal subtrai(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2) {
		return bigDecimal1.subtract(bigDecimal2);
	}

	/**
	 * Retorna o resultado arredondado para multiplica��o de dois objetos
	 * BigDecimal
	 * 
	 * @param bigDecimal1
	 * @param bigDecimal2
	 * @return bigDecimal1 * bigDecimal2
	 */
	public static BigDecimal multiplica(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2) {
		return arredonda(bigDecimal1.multiply(bigDecimal2), PRECISAO_PADRAO_CALCULO);
	}
	
	public static BigDecimal multiplicaSemArredondar(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2) {
		return bigDecimal1.multiply(bigDecimal2);
	}
	
	/**
	 * divide o bigDecimal1 pelo bigDecimal2 utilizando o arrendondamento padr�o
	 * e mantendo o mesmo n�mero de casas decimais dos objetos BigDecimal
	 * passados.
	 * 
	 * @param bigDecimal1
	 * @param bigDecimal2
	 * @return bigDecimal1 / bigDecimal2
	 */
	public static BigDecimal divideMantendoNumeroCasas(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2) {
		return bigDecimal1.divide(bigDecimal2, METODO_ARREDONDAMENTO_PADRAO);
	}

	/**
	 * divide o bigDecimal1 pelo bigDecimal2 utilizando a precis�o passada e com
	 * o arrendondamento padr�o
	 * 
	 * @param bigDecimal1
	 * @param bigDecimal2
	 * @return bigDecimal1 / bigDecimal2
	 */
	public static BigDecimal divide(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2) {
		return UtilNumero.divide(bigDecimal1, bigDecimal2, METODO_ARREDONDAMENTO_PADRAO);
	}
	
	public static BigDecimal divideSemArredondar(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2){
		return bigDecimal1.divide(bigDecimal2, PRECISAO_PADRAO_CALCULO, METODO_ARREDONDAMENTO_ROUND_DOWN);
	}

	/**
	 * faz a conta bigDecimal1 divido por bigDecimal2 com a precis�o padr�o
	 * definida pela constante PRECISAO_PADRAO_CALCULO e arredondada pelo
	 * roundingMode
	 * 
	 * <br>
	 * ROUND_CEILING = Rounding mode to round towards positive infinity. <br>
	 * <br>
	 * ROUND_DOWN = Rounding mode to round towards zero. <br>
	 * <br>
	 * ROUND_FLOOR = Rounding mode to round towards negative infinity. <br>
	 * <br>
	 * ROUND_HALF_DOWN = Rounding mode to round towards "nearest neighbor"
	 * unless both neighbors are equidistant, in which case round down. <br>
	 * <br>
	 * ROUND_HALF_EVEN = Rounding mode to round towards the "nearest neighbor"
	 * unless both neighbors are equidistant, in which case, round towards the
	 * even neighbor. <br>
	 * <br>
	 * ROUND_HALF_UP = Rounding mode to round towards "nearest neighbor" unless
	 * both neighbors are equidistant, in which case round up. <br>
	 * <br>
	 * ROUND_UNNECESSARY = Rounding mode to assert that the requested operation
	 * has an exact result, hence no rounding is necessary. <br>
	 * <br>
	 * ROUND_UP = Rounding mode to round away from zero.
	 */
	public static BigDecimal divide(final BigDecimal bigDecimal1, final BigDecimal bigDecimal2, int roundingMode) {

		if (UtilNumero.igual(BigDecimal.ZERO, bigDecimal1) && maior(bigDecimal2, BigDecimal.ZERO)) {
			return BigDecimal.ZERO;
		}

		return UtilNumero.retiraZerosDireita(bigDecimal1.divide(bigDecimal2, PRECISAO_PADRAO_CALCULO, roundingMode));
	}

	/**
	 * Verifica se o primeiro parametro � multiplo do segundo
	 * 
	 * @param numero
	 * @param multiplo
	 * @return
	 */
	public static Boolean isMultiplo(final Integer a, final Integer b) {
		return a % b == 0;
	}

	/**
	 * Verifica se o primeiro parametro � multiplo do segundo para BigDecimals
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Boolean isMultiplo(final BigDecimal a, final BigDecimal b) {

		final BigDecimal aFormatado = retiraZerosDireita(a);
		final BigDecimal bFormatado = retiraZerosDireita(b);

		/**
		 * o m�todo divideAndRemainder retorna um array, e a segunda posi��o
		 * representa o resto
		 */
		return UtilNumero.igual(BigDecimal.ZERO, aFormatado.divideAndRemainder(bFormatado)[1]);
	}

	/**
	 * Calcula a porcentagem do numerador em rela��o ao denominador. Ex:
	 * numerador = 1 e denominador = 2 retornar� 50
	 * 
	 * @param numerador
	 * @param denominador
	 * @return o valor da porcentagem
	 */
	public static Float calcularPorcentagem(final Float numerador, final Float denominador) {

		return (numerador / denominador) * 100;
	}

	/**
	 * Funciona como o m�todo calculaPorcentagem acima, a diferen�a � que este
	 * recebe e retorna BigDecimal
	 */
	public static BigDecimal calcularPorcentagem(BigDecimal numerador, BigDecimal denominador) {

		return multiplica(divide(numerador, denominador), new BigDecimal(100));
	}
	
	/**
	 * Funciona como o m�todo calculaPorcentagem acima, a diferen�a � que este
	 * recebe e retorna BigDecimal
	 */
	public static BigDecimal calcularPorcentagemSemArredondar(BigDecimal numerador, BigDecimal denominador) {

		return multiplicaSemArredondar(divideSemArredondar(numerador, denominador), new BigDecimal(100));
	}
	

	/**
	 * Retorna a porcentagem formatada com 2 casas decimais
	 * 
	 * @param numerador
	 * @param denominador
	 * @return
	 */
	public static String obterPorcentagemFormatada(final BigDecimal numerador, final BigDecimal denominador) {

		if (UtilNumero.maior(numerador, BigDecimal.ZERO) && UtilNumero.maior(denominador, BigDecimal.ZERO)) {

			return formatarNumeroComDuasCasasDecimaisComPonto(multiplica(divide(numerador, denominador), new BigDecimal("100")));

		} else {
			return "0";
		}
	}

	/**
	 * Calcula o resultado da porcentagem aplicada a um n�mero. Ex: numero = 10
	 * e porcentagem = 100 retornar� o pr�prio n�mero 10. numero = 10 e
	 * porcentagem = 80 retornar� 8.
	 * 
	 * @param numero
	 * @param porcentagem
	 *            valor da porcentagem multiplicada por 100 (ex: 78, 68.5, 19,
	 *            etc)
	 * @return o numerador da porcentagem
	 */
	public static BigDecimal obterResultadoPorcentagem(final BigDecimal numero, final BigDecimal porcentagem) {

		return multiplica(numero, divide(porcentagem, new BigDecimal("100")));
	}

	/**
	 * Retira os zeros a direita da parte fracion�ria. O n�mero 119.1000 ficar�
	 * 119.1
	 * 
	 * @param numero
	 * @return
	 */
	public static BigDecimal retiraZerosDireita(final BigDecimal bigDecimal) {

		if (Util.preenchido(bigDecimal)) {

			String numero = bigDecimal.toPlainString();
			String novoNumero = numero;
			final Integer indiceDecimal = novoNumero.indexOf(".");

			if (indiceDecimal > 0) {

				for (int i = numero.length() - 1; i > indiceDecimal; i--) {

					if (numero.charAt(i) == '0') {
						novoNumero = numero.substring(0, i);

					} else {
						break;
					}
				}
			}

			return new BigDecimal(novoNumero);

		} else {
			return null;
		}
	}

	/**
	 * Arredonda o bigDecimal passado para o n�mero de casas.
	 * 
	 * OBS.: Esse m�todo faz uso do m�todo retiraZerosDireita no resultado.
	 **/
	public static BigDecimal arredonda(BigDecimal numero, int casas) {
		return retiraZerosDireita(numero.setScale(casas, UtilNumero.METODO_ARREDONDAMENTO_PADRAO));
	}

	/**
	 * Arredonda o bigDecimal passado para o n�mero de casas.
	 * 
	 * OBS.:Quando o n�mero de casas n�o � passado o m�todo usa o valor da
	 * constante UtilNumero.PRECISAO_EXIBICAO.
	 **/
	public static BigDecimal arredonda(BigDecimal numero) {

		int PRECISAO_PADRAO_EXIBICAO = 3 ;
		int NUMERO_CASAS_DECIMAIS_APOS_ZERO = 2;

		String numeroFormatado = numero.toPlainString();
		numero = arredonda(numero, PRECISAO_PADRAO_EXIBICAO);
		final int posicaoVirgula = numeroFormatado.indexOf(".");
		int qtdeIteracao = 0;

		for (int i = posicaoVirgula + 1; i < numeroFormatado.length(); i++) {

			if (numeroFormatado.charAt(i) != '0') {
				break;
			}
			qtdeIteracao++;
		}

		if (qtdeIteracao + NUMERO_CASAS_DECIMAIS_APOS_ZERO < PRECISAO_PADRAO_EXIBICAO) {

			numero = arredonda(numero, qtdeIteracao + NUMERO_CASAS_DECIMAIS_APOS_ZERO);

		}

		return numero;
	}

	/**
	 * Arredonda o bigDecimal passado para o bigDecimal equivalente ao inteiro
	 * mais pr�ximo.
	 * 
	 * @param numero
	 * @return BigDecimal
	 */
	public static BigDecimal arredondaParaInteiroMaisProximo(BigDecimal numero) {

		numero = numero.setScale(0, BigDecimal.ROUND_HALF_UP);

		return numero;
	}

	/**
	 * Arredonda o bigDecimal passado para o bigDecimal equivalente ao inteiro
	 * mais pr�ximo acima.
	 * 
	 * @param numero
	 * @return BigDecimal
	 */
	public static BigDecimal arredondaParaInteiroAcima(BigDecimal numero) {

		numero = numero.setScale(0, BigDecimal.ROUND_CEILING);

		return numero;
	}

	/**
	 * Arredonda o bigDecimal passado para o bigDecimal equivalente ao inteiro
	 * mais pr�ximo abaixo.
	 * 
	 * @param numero
	 * @return BigDecimal
	 */
	public static BigDecimal arredondaParaInteiroAbaixo(BigDecimal numero) {

		numero = numero.setScale(0, BigDecimal.ROUND_DOWN);

		return numero;
	}

	/**
	 * Retorna o primeiro n�mero, menor que "numero" e que seja m�ltiplo de
	 * "valorMultiplo".
	 * 
	 * @param numero
	 * @param valorMultiplo
	 * @return
	 */
	public static BigDecimal multiploAnterior(BigDecimal numero, BigDecimal valorMultiplo) {
		return subtrai(numero, numero.divideAndRemainder(valorMultiplo)[1]);
	}

	public static BigDecimal multiploPosterior(BigDecimal numero, BigDecimal valorMultiplo) {

		if (isMultiplo(numero, valorMultiplo)) {
			return numero;
		}

		return soma(numero, subtrai(valorMultiplo, numero.divideAndRemainder(valorMultiplo)[1]));
	}

	/**
	 * Retorna o menor n�mero entre dois passados como par�metro.
	 * 
	 * @param valor1
	 * @param valor2
	 * @return
	 */
	public static BigDecimal min(BigDecimal valor1, BigDecimal valor2) {
		BigDecimal menor = valor1;
		if (maior(valor1, valor2)) {
			menor = valor2;
		}
		return menor;
	}

	/**
	 * Retorna o m�ltiplo mais proximo de "numero". Se a "dinst�ncia" entre os
	 * m�ltiplos for igual, retorna o menor.
	 * 
	 * @param numero
	 * @param bigDecimal2
	 * @return
	 */
	public static BigDecimal multiploMaisProximo(BigDecimal numero, BigDecimal valorMultiplo) {

		final BigDecimal anterior = multiploAnterior(numero, valorMultiplo);
		final BigDecimal posterior = multiploPosterior(numero, valorMultiplo);

		final BigDecimal distanciaPosterior = subtrai(posterior, numero);
		final BigDecimal distanciaAnterior = subtrai(numero, anterior);

		if (menor(distanciaPosterior, distanciaAnterior)) {
			return posterior;
		} else {
			return anterior;
		}
	}

	/**
	 * Converte o n�mero passado para BigDecimal
	 */
	public static BigDecimal converterParaBigDecimal(Number numero) {

		return formatarBigDecimal(formatarNumero(numero));
	}

	/**
	 * Calcula a media de uma lista de valoresu
	 */
	public static BigDecimal calculaMedia(List<BigDecimal> valores) {

		BigDecimal somatorio = BigDecimal.ZERO;

		for (BigDecimal valor : valores) {

			somatorio = UtilNumero.soma(somatorio, valor);
		}

		if (valores.size() > 0) {

			return UtilNumero.divide(somatorio, UtilNumero.converterParaBigDecimal(valores.size()));
		} else
			return BigDecimal.ZERO;
	}

	/**
	 * Obt�m o valor m�nimo de uma lista de valores passada como par�metro
	 */
	public static BigDecimal obtemValorMinimo(List<BigDecimal> valores) {

		if (Util.preenchido(valores)) {

			BigDecimal valorMinimo = new BigDecimal(Integer.MAX_VALUE);

			for (BigDecimal valor : valores) {

				if (UtilNumero.menor(valor, valorMinimo)) {
					valorMinimo = valor;
				}
			}

			return valorMinimo;
		} else
			return BigDecimal.ZERO;
	}

	/**
	 * Obt�m o valor m�ximo de uma lista de valores passada como par�metro
	 */
	public static BigDecimal obtemValorMaximo(List<BigDecimal> valores) {

		if (Util.preenchido(valores)) {

			BigDecimal valorMaximo = new BigDecimal(Integer.MIN_VALUE);

			for (BigDecimal valor : valores) {

				if (UtilNumero.maior(valor, valorMaximo)) {
					valorMaximo = valor;
				}
			}

			return valorMaximo;
		} else
			return BigDecimal.ZERO;
	}

	public static boolean isNumeroValido(Number numero) {

		try {

			formatarNumero(numero);
			return true;

		} catch (Exception e) {

			return false;
		}

	}
	
	/**
	 * Determina a quantidade de casas decimais do campo de acordo com o valor passado por par�metro.
	 * @param campo
	 * @param qtdeCasasDecimais
	 * @return
	 */
	public static String determinaQtdeCasasDecimais(String campo, Integer qtdeCasasDecimais){
		
		String resultado = null;
		
		if(Util.preenchido(campo) && UtilNumero.isNumber(campo)) {
			int escala = 0;
			
			final BigDecimal campoBig = new BigDecimal(campo);
			
			if(Util.preenchido(qtdeCasasDecimais)) {
				
				escala = qtdeCasasDecimais;
				
				resultado = UtilNumero.formatarNumero(campoBig, false, escala);

			} else {
				resultado = UtilNumero.formatarNumero(UtilNumero.arredonda(new BigDecimal(campo))); 
			}
			return resultado;
		}
		return campo;
	}
	
	public static String proximaString()
	  {
	    return new BigInteger(130, random).toString().substring(0, 5);
	  }
	
	public static String proximoUUID(){
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}
	
	public static double gerarNumeroAleatorio(){
				
		return random.nextDouble();
	}
}