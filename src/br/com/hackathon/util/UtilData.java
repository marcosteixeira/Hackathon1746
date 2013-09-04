package br.com.hackathon.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class UtilData {

	/**
	 * Quantidade de segundos em 1 minuto
	 */
	public static final int MINUTO = 60;

	/**
	 * Quantidade de segundos em 1 hora
	 */
	public static final int HORA = 3600;
	public static final BigDecimal HORA_DECIMAL = new BigDecimal("3600");

	private static final Logger log = Logger.getLogger(UtilData.class);

	/**
	 * Retorna uma string com o formato de data/hora desejado
	 * 
	 * @param gregorianCalendar
	 *            Data/hora que ser� formatada
	 * @param formato
	 *            Formato que deve ser utilizado
	 * @return A data/hora formatada
	 */
	public static String formata(GregorianCalendar gregorianCalendar, String formato) {

		if (gregorianCalendar == null) {
			return null;
		}

		SimpleDateFormat formatador = new SimpleDateFormat(formato);
		Date date = gregorianCalendarToDate(gregorianCalendar);
		String resultado = formatador.format(date);

		return resultado;
	}

	/**
	 * Veja o m�todo stringHora(GregorianCalendar)
	 */
	public static String stringHoraAtual() {
		return UtilData.stringHora(UtilData.agora());
	}

	/**
	 * Veja o m�todo stringData(GregorianCalendar)
	 */
	public static String stringDataAtual() {
		return UtilData.stringData(UtilData.agora());
	}

	/**
	 * Veja o m�todo stringDataCompleta(GregorianCalendar)
	 */
	public static String stringDataCompletaAtual() {
		return UtilData.stringDataCompleta(UtilData.agora());
	}

	/**
	 * Inst�ncia e retorna um GregorianCalendar, que j� vem com o a data/hora
	 * atual
	 */
	public static GregorianCalendar agora() {
		return new GregorianCalendar();
	}

	/**
	 * Soma dias a uma determinada data
	 * 
	 * @param data
	 * @param dias
	 * @return Uma string com a data resultante no formato "dd/mm/yyyy"
	 */
	public static String somaDias(GregorianCalendar data, int dias) {

		String dataFormatada = "";
		data.add(GregorianCalendar.DAY_OF_YEAR, dias);
		dataFormatada = UtilData.stringData(data);

		return dataFormatada;
	}

	public static String somaOuSubtraiDiasADataAtual(int dias) {

		return somaOuSubtraiDias(agora(), dias);
	}

	/**
	 * Soma ou subtrai dias da data atual de acordo com o valor da vari�vel
	 * dias. Se for um valor positivo, soma. Se for negativo, subtrai.
	 */
	public static String somaOuSubtraiDias(GregorianCalendar data, int dias) {

		if (dias > 0) {
			return somaDias(data, dias);
		}
		if (dias < 0) {
			return stringData(subtraiDias(dias * -1, data));
		}
		return stringData(data);
	}

	/**
	 * Soma dias a data atual
	 * 
	 * @param dias
	 * @return
	 */
	public static String somaDiasADataAtual(int dias) {
		return UtilData.somaDiasAData(agora(), dias);
	}

	public static String somaDiasAData(GregorianCalendar data, int dias) {

		String dataFormatada = "";
		data.add(GregorianCalendar.DAY_OF_YEAR, dias);
		dataFormatada = UtilData.stringData(data);
		return dataFormatada;
	}

	/**
	 * Subtrai dias da data atual
	 * 
	 * @param dias
	 *            passado em m�dulo (passe um numero positivo!!!)
	 * @return data com formato dd/mm/aa
	 */
	public static String subtraiDiasADataAtual(int dias) {

		String dataFormatada = "";
		dias = dias - (dias * 2);
		GregorianCalendar dataAtual = new GregorianCalendar();

		dataAtual.add(GregorianCalendar.DAY_OF_YEAR, dias);
		dataFormatada = UtilData.stringData(dataAtual);
		return dataFormatada;
	}

	/**
	 * Subtrai dias da data
	 * 
	 * @param dias
	 * @param data
	 * @return uma nova data no formato GregorianCalendar
	 */
	public static GregorianCalendar subtraiDias(int dias, GregorianCalendar data) {

		GregorianCalendar data2 = (GregorianCalendar) data.clone();
		dias = dias - (dias * 2);
		data2.add(GregorianCalendar.DAY_OF_YEAR, dias);
		return data2;
	}

	/**
	 * Subtrai meses da data
	 * 
	 * @param meses
	 * @param data
	 * @return uma nova data no formato GregorianCalendar
	 */
	public static GregorianCalendar subtraiMeses(int meses, GregorianCalendar data) {

		GregorianCalendar data2 = (GregorianCalendar) data.clone();
		meses = meses - (meses * 2);
		data2.add(GregorianCalendar.MONTH, meses);
		return data2;
	}

	/**
	 * Somas dias � data
	 * 
	 * @param dias
	 * @param data
	 * @return uma nova data no formato GregorianCalendar
	 */
	public static GregorianCalendar somaDias(int dias, GregorianCalendar data) {

		GregorianCalendar data2 = (GregorianCalendar) data.clone();
		data2.add(GregorianCalendar.DAY_OF_YEAR, dias);
		return data2;
	}
	
	/**
	 * Somas segundos � data
	 * 
	 * @param segundos
	 * @param data
	 * @return uma nova data no formato GregorianCalendar
	 */
	public static GregorianCalendar somaSegundos(int segundos, GregorianCalendar data) {
		
		GregorianCalendar data2 = (GregorianCalendar) data.clone();
		data2.add(GregorianCalendar.SECOND, segundos);
		return data2;
	}
	
	public static String somaMeses(int meses, GregorianCalendar data){
		
		data.add(GregorianCalendar.MONTH, meses);
		return UtilData.stringData(data);
	}
	
	public static String somaAnos(int anos, GregorianCalendar data){
		
		data.add(GregorianCalendar.YEAR, anos);
		return UtilData.stringData(data);
	}

	/**
	 * <p>
	 * Recebe uma string que forma uma data(dd/mm/aaaa) divide-a em 3 partes
	 * pega a primeira(o dia).
	 * </p>
	 * <p>
	 * Este m�todo retorna um int.
	 * </p>
	 * 
	 * @param data
	 *            - string a ser analisada .
	 * @return "dia"
	 * @throws RuntimeException
	 *             - se o formato nao for reconhecido.
	 */
	public static int getDia(String data) throws RuntimeException {

		int dia = 0;
		int d = 0;
		StringTokenizer tokenizer = new StringTokenizer(data, "/");

		if (tokenizer.countTokens() != 3) {
			throw new RuntimeException("A data n�o est� no formato correto (dd/mm/aaaa)");
		}

		d = Integer.parseInt((String) tokenizer.nextElement());

		if (d <= 0 || d > 31) {
			throw new RuntimeException("O dia deve ser um n�mero no intervalo entre 0 e 31");
		} else {
			dia = d;
		}

		return dia;
	}
	
	public static int getDiaEUA(String data) throws RuntimeException {

		int dia = 0;
		int d = 0;
		StringTokenizer tokenizer = new StringTokenizer(data, "/");

		if (tokenizer.countTokens() != 3) {
			throw new RuntimeException("A data n�o est� no formato correto (dd/mm/aaaa)");
		}
		tokenizer.nextElement();
		tokenizer.nextElement();
		d = Integer.parseInt((String) tokenizer.nextElement());

		if (d <= 0 || d > 31) {
			throw new RuntimeException("O dia deve ser um n�mero no intervalo entre 0 e 31");
		} else {
			dia = d;
		}

		return dia;
	}

	/**
	 * <p>
	 * Recebe uma string que forma uma data(dd/mm/aaaa) divide-a em 3 partes
	 * pega a segunda (o mes).
	 * </p>
	 * <p>
	 * Este m�todo retorna um int.
	 * </p>
	 * 
	 * @param data
	 *            - string a ser analisada .
	 * @return "mes"
	 * @throws RuntimeException
	 *             - se o formato nao for reconhecido.
	 */
	public static int getMes(String data) throws RuntimeException {

		int mes = 0;
		int m = 0;
		StringTokenizer tokenizer = new StringTokenizer(data, "/");

		if (tokenizer.countTokens() != 3) {
			throw new RuntimeException("A data n�o est� no formato correto (dd/mm/aaaa)");
		}

		tokenizer.nextElement();

		m = Integer.parseInt((String) tokenizer.nextElement());

		if (m <= 0 || m > 12) {
			throw new RuntimeException("O m�s deve ser um n�mero no intervalo entre 1 e 12");
		} else {
			mes = m;
		}

		return mes - 1;
	}

	/**
	 * <p>
	 * Recebe uma string que forma uma data(dd/mm/aaaa) divide-a em 3 partes
	 * pega a terceira(o ano).
	 * </p>
	 * <p>
	 * Este m�todo retorna um int.
	 * </p>
	 * 
	 * @param data
	 *            - string a ser analisada .
	 * @return "ano"
	 * @throws RuntimeException
	 *             - se o formato nao for reconhecido.
	 */
	public static int getAno(String data) throws RuntimeException {

		int ano = 0;
		String anoString = "";
		StringTokenizer tokenizer = new StringTokenizer(data, "/");
		Calendar calendar = Calendar.getInstance();
		int anoCorrente = calendar.get(Calendar.YEAR);

		if (tokenizer.countTokens() != 3) {
			throw new RuntimeException("A data n�o est� no formato correto (dd/mm/aaaa)");
		} else {
			tokenizer.nextElement();
			tokenizer.nextElement();
			// ano = Integer.parseInt((String) tokenizer.nextElement());
			anoString = ((String) tokenizer.nextElement()).trim();

			ano = Integer.parseInt(UtilString.retiraZeroEsquerda(anoString));

			// ano >100 indica que o ano tem 4 d�gitos...
			// se tiver 3 estar� errado...
			// nesse caso ano so pode ter 2 ou 4 d�gitos
			if (ano < 100) { // 2 d�gito - aqui o ano de 2 digitos e
				// transformado para 4
				String anoCorrenteStr = String.valueOf(anoCorrente);
				anoString = anoCorrenteStr.substring(0, 2) + // pega os dois
				// primeiros
				anoString;
				ano = Integer.parseInt(anoString);

			}

			if (ano < 1980) {
				throw new RuntimeException("O ano n�o pode ser anterior a 1980");
			}
		}

		return ano;
	}
	
	public static int getAnoEUA(String data) throws RuntimeException {

		int ano = 0;
		String anoString = "";
		StringTokenizer tokenizer = new StringTokenizer(data, "/");
		Calendar calendar = Calendar.getInstance();
		int anoCorrente = calendar.get(Calendar.YEAR);

		if (tokenizer.countTokens() != 3) {
			throw new RuntimeException("A data n�o est� no formato correto (dd/mm/aaaa)");
		} else {
			// ano = Integer.parseInt((String) tokenizer.nextElement());
			anoString = ((String) tokenizer.nextElement()).trim();

			ano = Integer.parseInt(UtilString.retiraZeroEsquerda(anoString));

			// ano >100 indica que o ano tem 4 d�gitos...
			// se tiver 3 estar� errado...
			// nesse caso ano so pode ter 2 ou 4 d�gitos
			if (ano < 100) { // 2 d�gito - aqui o ano de 2 digitos e
				// transformado para 4
				String anoCorrenteStr = String.valueOf(anoCorrente);
				anoString = anoCorrenteStr.substring(0, 2) + // pega os dois
				// primeiros
				anoString;
				ano = Integer.parseInt(anoString);

			}

			if (ano < 1980) {
				throw new RuntimeException("O ano n�o pode ser anterior a 1980");
			}
		}

		return ano;
	}

	/**
	 * <p>
	 * Recebe uma string que forma uma hora conta at� o : e pega a primeira
	 * parte da string.
	 * </p>
	 * <p>
	 * Este m�todo retorna um int.
	 * </p>
	 * 
	 * @param hora
	 *            - string a ser analisada .
	 * @return "hora"
	 * @throws RuntimeException
	 *             - se o formato nao for reconhecido.
	 */
	public static int getHora(String horas) throws RuntimeException {

		int hora = 0;
		int h = 0;
		StringTokenizer tokenizer = new StringTokenizer(horas, ":");

		if (tokenizer.countTokens() != 2) {
			throw new RuntimeException("A hora n�o est� no formato correto (hh:mm)");
		}

		h = Integer.parseInt((String) tokenizer.nextElement());

		if (h > 23) {
			throw new RuntimeException("A hora n�o pode ser maior que 23");
		} else {
			hora = h;
		}

		return hora;
	}

	/**
	 * <p>
	 * Recebe uma string que forma uma hora conta at� o : e pega a segunda parte
	 * da string.
	 * </p>
	 * <p>
	 * Este m�todo retorna um int.
	 * </p>
	 * 
	 * @param hora
	 *            - string a ser analisada .
	 * @return "minuto" ou entra em uma exce��o
	 * @throws RuntimeException
	 *             - se o formato nao for reconhecido.
	 */
	public static int getMinuto(String horas) throws RuntimeException {

		int minuto = 0;
		int m = 0;
		StringTokenizer tokenizer = new StringTokenizer(horas, ":");

		if (tokenizer.countTokens() != 2) {
			throw new RuntimeException("A hora n�o est� no formato correto (hh:mm)");
		}

		tokenizer.nextElement();
		m = Integer.parseInt((String) tokenizer.nextElement());

		if (m > 59) {
			throw new RuntimeException("O minuto n�o pode ser maior que 59");
		} else {
			minuto = m;
		}

		return minuto;
	}

	/**
	 * Converte um GregorianCalendar em uma string com a hora inclusive a parte
	 * dos segundos
	 * 
	 * @return String com a hora no formato hh:mm:ss
	 */
	public static String stringHoraCompleta(GregorianCalendar dataHora) {
		return formata(dataHora, "HH:mm:ss");
	}

	/**
	 * Converte um GregorianCalendar em uma string com a hora
	 * 
	 * @return String com a hora no formato hh:mm
	 */
	public static String stringHora(GregorianCalendar dataHora) {
		return formata(dataHora, "HH:mm");
	}

	/**
	 * Converte um GregorianCalendar em uma string com a data
	 * 
	 * @return String com a data no formato dd/mm/aa
	 */
	public static String stringData(GregorianCalendar dataHora) {
		return formata(dataHora, "dd/MM/yyyy");
	}

	/**
	 * Converte um GregorianCalendar em uma string com o m�s e o ano.
	 * 
	 * @return String com a data no formato mm/aa
	 */
	public static String stringMesAno(GregorianCalendar dataHora) {
		return formata(dataHora, "MM/yy");
	}

	/**
	 * Converte um GregorianCalendar em uma string com a data com o ano completo
	 * 
	 * @param dataHora
	 * @return data no formato dd/mm/aaaa
	 */
	public static String stringDataCompleta(GregorianCalendar dataHora) {
		return formata(dataHora, "dd/MM/yyyy");
	}

	/**
	 * Converte um GregorianCalendar em uma string com data hora completa sem
	 * segundos.
	 * 
	 * @return String com a data no formato dd/mm/aa HH:mm
	 */
	public static String stringDataHora(GregorianCalendar dataHora) {
		return formata(dataHora, "dd/MM/yy HH:mm");
	}

	/**
	 * Converte um GregorianCalendar em uma string com data hora completa.
	 * 
	 * @return String com a data no formato dd/mm/aa HH:mm:ss
	 */
	public static String stringDataHoraCompleta(GregorianCalendar dataHora) {
		return formata(dataHora, "dd/MM/yyyy HH:mm:ss");
	}
	/**
	 * Converte um GregorianCalendar em uma string com data hora completa.
	 * 
	 * @return String com a data no formato dd/mm/aa HH:mm:ss
	 */
	public static String stringDataHoraCompletaRetornandoNull(GregorianCalendar dataHora) {
		if(Util.preenchido(dataHora)) {
			return formata(dataHora, "dd/MM/yyyy HH:mm:ss");
		} else {
			return null;
		}
	}

	/**
	 * Converte um GregorianCalendar em uma string com data hora completa no padrão DATETIME SQL.
	 * 
	 * @return String com a data no formato dd/mm/aa HH:mm:ss
	 */
	public static String stringDataHoraCompletaSQL(GregorianCalendar dataHora) {
		return formata(dataHora, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String stringDataHoraCompletaAmericana(GregorianCalendar dataHora) {
		return formata(dataHora, "MM/dd/yyyy HH:mm:ss");
	}

	/**
	 * @return String com a hora no formato hh:mm
	 */
	public static String formatarHora(int hora, int minuto) {

		StringBuffer horario = new StringBuffer();

		/*
		 * a l�gica abaixo serve para evitar que um hor�rio como 09:02 apare�a
		 * como 9:2
		 */
		if (hora < 10) {
			horario.append('0');
		}

		horario.append(hora);
		horario.append(':');

		if (minuto < 10) {
			horario.append('0');
		}

		horario.append(minuto);

		return horario.toString();
	}

	public static GregorianCalendar getCalendar(String data, String horas) throws RuntimeException {

		int dia = getDia(data);
		int mes = getMes(data);
		int ano = getAno(data);

		int hora = 0;
		int minuto = 0;

		if (horas != null) {
			hora = getHora(horas);
			minuto = getMinuto(horas);
		}

		return new GregorianCalendar(ano, mes, dia, hora, minuto);
	}

	/**
	 * Chama o getCalendar(String) e retorna null caso n�o seja poss�vel
	 * converter
	 */
	public static GregorianCalendar getCalendarTratado(String data) {

		try {
			return getCalendar(data);

		} catch (Exception e) {
			return null;
		}
	}

	public static GregorianCalendar getCalendar(String data) throws RuntimeException {

		int dia = getDia(data);
		int mes = getMes(data);
		int ano = getAno(data);

		return new GregorianCalendar(ano, mes, dia);
	}
	
	public static GregorianCalendar getCalendarEUA(String data) throws RuntimeException {
		String dataFinal = "";
		if(data.contains("-")){
			dataFinal = data.replace("-", "/");
		}else{
			dataFinal = data;
		}

		int dia = getDiaEUA(dataFinal);
		int mes = getMes(dataFinal);
		int ano = getAnoEUA(dataFinal);

		return new GregorianCalendar(ano, mes, dia);
	}
	
	
	
	public static boolean isDataValida(String data) {

		try {
			getDia(data);
			getMes(data);
			getAno(data);

		} catch (Exception e) {
			return false;
		}

		return true;

	}

	public static boolean isHoraValida(String hora) {

		return UtilString.verificarFormato(hora, "(([0-1][0-9]|2[0-3]):[0-5][0-9])");

	}

	/**
	 * Este m�todo valida se a hora est� dentro do formato permitido para horas
	 * que variam de 00:00 � 24:00 (inclusive). � permitidos apenas o formato
	 * HH:MM (de 00:00 � 24:00).
	 * 
	 * @param hora
	 * @return
	 */
	public static boolean isHoraValidaVinteQuatroHoras(String hora) {

		return UtilString.verificarFormato(hora, "(([0-1][0-9]|2[0-3]):[0-5][0-9])|24:00");
	}

	/**
	 * Retorna a dura��o do apontamento
	 * 
	 * @param dataHoraInicio
	 * @param dataHoraFim
	 * @return dura��o do apontamento em horas
	 * @deprecated Esse m�todo ser� apagado, utilizar
	 *             calculaDuracaoHoras(GregorianCalendar,GregorianCalendar)
	 */
	public static double calculaDuracaoHoras2(GregorianCalendar dataHoraInicio, GregorianCalendar dataHoraFim) {

		double inicio = dataHoraInicio.getTimeInMillis();
		double fim = dataHoraFim.getTimeInMillis();
		double duracaoMilisegundos = fim - inicio;
		double duracao = duracaoMilisegundos / 3600000D;

		return duracao;
	}

	/**
	 * @param dataHoraInicio
	 * @param dataHoraFim
	 * @return Dura��o em horas.
	 */
	public static float calculaDuracaoHoras(GregorianCalendar dataHoraInicio, GregorianCalendar dataHoraFim) throws RuntimeException {

		float duracao;

		if (dataHoraFim == null || dataHoraInicio == null) {
			throw new RuntimeException("Os par�metros \"dataHoraInicio\" e \"dataHoraFim\" n�o podem ser null");
		}

		duracao = dataHoraFim.getTimeInMillis() - dataHoraInicio.getTimeInMillis();
		duracao = duracao / 3600000;

		return duracao;
	}

	/**
	 * Resultado = dataFim - DataInicio
	 * 
	 * @param dataHoraInicio
	 * @param dataHoraFim
	 * @return Dura��o em Dias.
	 */
	public static int calculaDuracaoDias(GregorianCalendar dataHoraInicio, GregorianCalendar dataHoraFim) throws RuntimeException {

		float duracaoEmHoras = calculaDuracaoHoras(dataHoraInicio, dataHoraFim);
		int duracaoEmDias = (int) (duracaoEmHoras / 24);

		return duracaoEmDias;
	}

	/**
	 * Resultado = dataFim - DataInicio
	 * 
	 * @param dataHoraInicio
	 * @param dataHoraFim
	 * @return Dura��o em Dias.
	 */
	public static Integer calculaDuracaoDiasArredondaPraCima(GregorianCalendar dataHoraInicio, GregorianCalendar dataHoraFim) throws RuntimeException {

		long duracao = dataHoraFim.getTimeInMillis() - dataHoraInicio.getTimeInMillis();

		BigDecimal duracaoEmDias = UtilNumero.arredondaParaInteiroAcima(UtilNumero.divide(new BigDecimal(duracao), new BigDecimal("86400000"), BigDecimal.ROUND_UP));

		return duracaoEmDias.intValueExact();
	}

	public static int calculaDuracaoSegundos(GregorianCalendar dataHoraInicio, GregorianCalendar dataHoraFim) {

		long duracao = 0;

		// duracao em milisegundos
		duracao = dataHoraFim.getTimeInMillis() - dataHoraInicio.getTimeInMillis();

		// duracao em segundos
		duracao = duracao / 1000;

		return (int) duracao;
	}

	/**
	 * Calcula diferen�a em minutos. Retorna o m�dulo da diferen�a, pois a
	 * primeira data pode ser posterior a segunda, o que geraria um resultado
	 * negativo.
	 * 
	 * @param dataHora1
	 *            primeira dataHora
	 * @param dataHora2
	 *            segunda dataHora
	 * @return M�dulo da diferen�a em minutos
	 */
	public static float calculaDiferencaMinutosModulo(GregorianCalendar dataHora1, GregorianCalendar dataHora2) {
		return Math.abs(UtilData.calculaDiferencaMinutos(dataHora1, dataHora2));
	}

	/**
	 * Calcula diferen�a em minutos. Caso a dataHora1 seja posterior a
	 * dataHora2, o resultado ser� negativo.
	 * 
	 * @param dataHora1
	 *            primeira dataHora
	 * @param dataHora2
	 *            segunda dataHora
	 * @return Diferen�a em minutos
	 * @throws RuntimeException
	 *             Caso um dos par�metros seja null
	 */
	public static float calculaDiferencaMinutos(GregorianCalendar dataHora1, GregorianCalendar dataHora2) throws RuntimeException {

		BigDecimal diferenca;

		if (dataHora1 == null || dataHora2 == null) {
			throw new RuntimeException("Os par�metros \"dataHora1\" e \"dataHora2\" n�o podem ser null");
		}

		diferenca = UtilNumero.subtrai(BigDecimal.valueOf(dataHora1.getTimeInMillis()), BigDecimal.valueOf(dataHora2.getTimeInMillis()));
		diferenca = UtilNumero.divideMantendoNumeroCasas(diferenca, new BigDecimal(60000));

		return diferenca.floatValue();
	}

	public static String formataDiaHora(int dia, int hora, int minuto) {

		StringBuffer dataHora = new StringBuffer();

		if (dia > 0) {
			dataHora.append(dia);
			dataHora.append("d ");

			dataHora.append(hora);
			dataHora.append("h ");

			dataHora.append(minuto);
			dataHora.append('m');

		} else if (hora > 0) {
			dataHora.append(hora);
			dataHora.append("h ");

			dataHora.append(minuto);
			dataHora.append('m');

		} else if (minuto > 0) {
			dataHora.append(minuto);
			dataHora.append('m');

		} else {
			dataHora.append("0m");
		}

		return dataHora.toString().trim();
	}

	public static String formataDiaHora(int dia, int hora) {

		StringBuffer dataHora = new StringBuffer();

		if (dia > 0) {
			dataHora.append(dia);
			dataHora.append("d ");

			dataHora.append(hora);
			dataHora.append('h');

		} else if (hora > 0) {
			dataHora.append(hora);
			dataHora.append('h');

		} else {
			dataHora.append("0m");
		}

		return dataHora.toString().trim();
	}

	public static int converteStringHoraEmIntMinuto(String hora) throws Exception {

		String[] horas = hora.split(":");
		String[] arrayAux;
		String primeiroValor;
		int qtdeDias;
		int qtdeHoras;
		int qtdeMinutos;

		if (horas.length > 1) {
			int hh = Integer.parseInt(horas[0]);
			int mm = Integer.parseInt(horas[1]);

			if (mm > 59) {
				throw new Exception("O n�mero de minutos n�o pode ser maior que 59");
			}

			return (hh * MINUTO) + mm;

		} else {
			arrayAux = hora.split(" ");

			if (arrayAux.length == 3) {
				arrayAux[0] = arrayAux[0].replace("d", "");
				arrayAux[1] = arrayAux[1].replace("h", "");
				arrayAux[2] = arrayAux[2].replace("m", "");

				qtdeDias = Integer.parseInt(arrayAux[0].trim());
				qtdeHoras = Integer.parseInt(arrayAux[1].trim());
				qtdeMinutos = Integer.parseInt(arrayAux[2].trim());

				return qtdeDias * 1440 + qtdeHoras * 24 + qtdeMinutos;

			} else if (arrayAux.length == 2) {
				primeiroValor = arrayAux[0];

				if (primeiroValor.contains("d")) {
					arrayAux[0] = arrayAux[0].replace("d", "");
					arrayAux[1] = arrayAux[1].replace("h", "");

					qtdeDias = Integer.parseInt(arrayAux[0].trim());
					qtdeHoras = Integer.parseInt(arrayAux[1].trim());

					return qtdeDias * 1440 + qtdeHoras;

				} else {
					arrayAux[0] = arrayAux[0].replace("h", "");
					arrayAux[1] = arrayAux[1].replace("m", "");

					qtdeHoras = Integer.parseInt(arrayAux[0].trim());
					qtdeMinutos = Integer.parseInt(arrayAux[1].trim());

					return qtdeHoras * 24 + qtdeMinutos;
				}

			} else {
				arrayAux[0] = arrayAux[0].replace("m", "");
				primeiroValor = arrayAux[0];

				if (primeiroValor.contains("h")) {
					arrayAux[0] = arrayAux[0].replace("h", "");

					qtdeHoras = Integer.parseInt(arrayAux[0].trim());
					return qtdeHoras * 24;

				} else {
					qtdeMinutos = Integer.parseInt(arrayAux[0].trim());
					return qtdeMinutos;
				}
			}
		}
	}

	public static GregorianCalendar dateToGregorianCalendar(Date date) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);

		return gregorianCalendar;
	}

	public static Date gregorianCalendarToDate(GregorianCalendar gregorianCalendar) {

		String segundo = String.valueOf(gregorianCalendar.get(GregorianCalendar.SECOND));
		String minuto = String.valueOf(gregorianCalendar.get(GregorianCalendar.MINUTE));
		String hora = String.valueOf(gregorianCalendar.get(GregorianCalendar.HOUR_OF_DAY));
		String dia = String.valueOf(gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH));
		String mes = String.valueOf(gregorianCalendar.get(GregorianCalendar.MONTH) + 1);
		String ano = String.valueOf(gregorianCalendar.get(GregorianCalendar.YEAR));

		SimpleDateFormat formatador = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		Date date = null;

		try {
			date = formatador.parse(hora + ":" + minuto + ":" + segundo + " " + dia + "/" + mes + "/" + ano);
		} catch (ParseException e) {
			log.error("N�o foi poss�vel converter um objeto GregorianCalendar para Date", e);
		}

		return date;
	}

	public static String getPrimeiroDiaAno(GregorianCalendar dataAtual) {

		int ano = dataAtual.get(GregorianCalendar.YEAR);

		return "01/01/" + String.valueOf(ano).substring(2);
	}

	public static String getUltimoDiaAno(GregorianCalendar dataAtual) {

		int ano = dataAtual.get(GregorianCalendar.YEAR);

		return "31/12/" + String.valueOf(ano).substring(2);
	}

	public static String getPrimeiroDiaMes(GregorianCalendar dataAtual) {

		int mes = dataAtual.get(GregorianCalendar.MONTH) + 1;
		int ano = dataAtual.get(GregorianCalendar.YEAR);

		return "01/" + (mes < 10 ? "0" : "") + mes + "/" + String.valueOf(ano).substring(2);
	}

	/**
	 * Quando o gregorian calendar � constru�do apenas pelo dia, m�s e ano a
	 * hora fica sendo meia-noite. Na pesquisa isso � inadequado porque tudo o
	 * que aconteceu ap�s a meia-noite � desconsiderado. O m�todo tenta resolver
	 * este problema setando a hora 23:59:59 no gregorian calendar
	 * 
	 * @param data
	 * @return a data passada com a hora 23:59:59
	 */
	public static GregorianCalendar completaGregorianCalendar(GregorianCalendar data) {

		int hora = 23;
		int minutos = 59;
		int segundos = 59;
		return new GregorianCalendar(data.get(GregorianCalendar.YEAR), data.get(GregorianCalendar.MONTH), data.get(GregorianCalendar.DAY_OF_MONTH), hora, minutos, segundos);
	}

	public static List<String> listarMesesAno(GregorianCalendar dataInicio, GregorianCalendar dataFim) {

		int mes = dataInicio.get(GregorianCalendar.MONTH);
		int ano = dataInicio.get(GregorianCalendar.YEAR);
		int mesFim = dataFim.get(GregorianCalendar.MONTH);
		int anoFim = dataFim.get(GregorianCalendar.YEAR);

		String mesAno;
		List<String> mesesAno = new ArrayList<String>();

		while ((ano < anoFim) || (ano == anoFim && mes <= mesFim)) {
			switch (mes) {
			case 0:
				mesAno = "Jan/";
				break;
			case 1:
				mesAno = "Fev/";
				break;
			case 2:
				mesAno = "Mar/";
				break;
			case 3:
				mesAno = "Abr/";
				break;
			case 4:
				mesAno = "Mai/";
				break;
			case 5:
				mesAno = "Jun/";
				break;
			case 6:
				mesAno = "Jul/";
				break;
			case 7:
				mesAno = "Ago/";
				break;
			case 8:
				mesAno = "Set/";
				break;
			case 9:
				mesAno = "Out/";
				break;
			case 10:
				mesAno = "Nov/";
				break;
			case 11:
				mesAno = "Dez/";
				break;
			default:
				throw new RuntimeException("O m�s n�o foi definido corretamente");
			}

			mesAno += ano;
			mesesAno.add(mesAno);

			if (mes < 11) {
				mes++;
			} else {
				mes = 0;
				ano++;
			}
		}

		return mesesAno;
	}

	public static String calculaDuracaoHorasFormatado(GregorianCalendar dataHoraInicio, GregorianCalendar dataHoraFim) {

		int hora;
		int minuto;
		float minutosTotais;

		if (dataHoraFim == null) {
			dataHoraFim = new GregorianCalendar();
		}
		minutosTotais = calculaDiferencaMinutosModulo(dataHoraInicio, dataHoraFim);
		hora = (int) minutosTotais / MINUTO;
		minuto = (int) minutosTotais % MINUTO;

		return formatarHora(hora, minuto);
	}

	public static String formataHora(int minutos) {

		int hora = minutos / MINUTO;
		int minuto = minutos % MINUTO;

		return (hora < 10 ? "0" : "") + hora + ":" + (minuto < 10 ? "0" : "") + minuto;
	}

	/**
	 * Verifica se o m�s e o ano das duas datas s�o iguais.
	 * 
	 * @param gregorianCalendar1
	 * @param gregorianCalendar2
	 * @return true se o m�s e o ano forem iguais, false caso contr�rio.
	 */
	public static boolean isMesmoMesAno(GregorianCalendar gregorianCalendar1, GregorianCalendar gregorianCalendar2) {

		if (gregorianCalendar1 != null && gregorianCalendar2 != null) {
			int mes1 = gregorianCalendar1.get(GregorianCalendar.MONTH);
			int ano1 = gregorianCalendar1.get(GregorianCalendar.YEAR);
			int mes2 = gregorianCalendar2.get(GregorianCalendar.MONTH);
			int ano2 = gregorianCalendar2.get(GregorianCalendar.YEAR);

			if (mes1 == mes2 && ano1 == ano2) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica se o dia, m�s e o ano das duas datas s�o iguais.
	 * 
	 * @param gregorianCalendar1
	 * @param gregorianCalendar2
	 * @return true se o dia,m�s e o ano forem iguais, false caso contr�rio.
	 */
	public static boolean isMesmoDiaMesAno(GregorianCalendar gregorianCalendar1, GregorianCalendar gregorianCalendar2) {

		if (gregorianCalendar1 != null && gregorianCalendar2 != null) {
			int dia1 = gregorianCalendar1.get(GregorianCalendar.DAY_OF_MONTH);
			int mes1 = gregorianCalendar1.get(GregorianCalendar.MONTH);
			int ano1 = gregorianCalendar1.get(GregorianCalendar.YEAR);
			int dia2 = gregorianCalendar2.get(GregorianCalendar.DAY_OF_MONTH);
			int mes2 = gregorianCalendar2.get(GregorianCalendar.MONTH);
			int ano2 = gregorianCalendar2.get(GregorianCalendar.YEAR);

			if (mes1 == mes2 && ano1 == ano2 && dia1 == dia2) {
				return true;
			}
		}
		return false;
	}

	/**
	 * M�todo que converte o tempo de hh:mm:ss ou hh:mm para segundos
	 * 
	 * @param HHMMSS
	 *            String no formato hh:mm:ss ou hh:mm
	 * @return Integer o tempo em segundos, ou nulo se HHMMSS for vazio
	 */
	public static Integer converteMascaraParaSegundos(String HHMMSS) {

		if (Util.vazio(HHMMSS)) {
			return 0;
		}

		int hora;
		int minuto;
		int segundo;

		String[] tempo = HHMMSS.split(":");
		Integer tempoConvertido;

		if (tempo.length == 2) {
			hora = Integer.valueOf(tempo[0]);
			minuto = Integer.valueOf(tempo[1]);
			tempoConvertido = (hora * HORA) + (minuto * MINUTO);

		} else if (tempo.length == 3) {
			hora = Integer.valueOf(tempo[0]);
			minuto = Integer.valueOf(tempo[1]);
			segundo = Integer.valueOf(tempo[2]);
			tempoConvertido = (hora * HORA) + (minuto * MINUTO) + segundo;

		} else {
			tempoConvertido = Integer.valueOf(tempo[0]);
		}

		return tempoConvertido;
	}

	/**
	 * Retorna true se a String est� no formato hh:mm:ss, ou false caso
	 * contr�rio.
	 */
	public static boolean isMascaraOK(final String HHMMSS) {

		if (HHMMSS != null) {
			final String[] tempo = HHMMSS.split(":");

			if (tempo.length == 2) {

				if (UtilNumero.isInt(tempo[0]) && UtilNumero.isInt(tempo[1])) {
					return true;
				}

			} else if (tempo.length == 3) {

				if (UtilNumero.isInt(tempo[0]) && UtilNumero.isInt(tempo[1]) && UtilNumero.isInt(tempo[2])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Converte segundos para mascara retornando apenas horas e segundos
	 * 
	 * Ex: 12:00 em vez de 12:00:00
	 */
	public static String converteSegundosParaMascaraApenasComHorasEMinutos(final Integer segundos) {

		String resultado = null;

		if (segundos != null) {

			int hora = segundos / HORA;
			int minuto = (segundos / MINUTO) - (hora * MINUTO);

			resultado = String.valueOf((hora < 10 ? "0" + hora : hora));
			resultado += ":" + (minuto < 10 ? "0" + minuto : minuto);
		}
		return resultado;
	}

	/**
	 * M�todo que converte de segundos para a m�scara
	 * 
	 * @param segundos
	 *            o tempo em segundos
	 * @return String a m�scara
	 */
	public static String converteSegundosParaMascara(final Integer segundos) {

		String resultado = null;

		if (segundos != null) {

			int hora = segundos / HORA;
			int minuto = (segundos / MINUTO) - (hora * MINUTO);
			int segundo = segundos % MINUTO;

			resultado = String.valueOf((hora < 10 ? "0" + hora : hora));
			resultado += ":" + (minuto < 10 ? "0" + minuto : minuto);
			resultado += ":" + (segundo < 10 ? "0" + segundo : segundo);
		}
		return resultado;
	}

	/**
	 * M�todo que converte de segundos para a m�scara considerando segundos
	 * negativo
	 * 
	 * @param segundos
	 *            o tempo em segundos
	 * @return String a m�scara
	 */
	public static String converteSegundosParaMascaraNegativo(Integer segundos) {

		String resultado = null;
		boolean negativo = false;

		if (segundos != null) {

			if (segundos < 0) {
				segundos = Math.abs(segundos);
				negativo = true;
			}
			int hora = segundos / HORA;
			int minuto = (segundos / MINUTO) - (hora * MINUTO);
			int segundo = segundos % MINUTO;

			resultado = String.valueOf((hora < 10 ? "0" + hora : hora));
			resultado += ":" + (minuto < 10 ? "0" + minuto : minuto);
			resultado += ":" + (segundo < 10 ? "0" + segundo : segundo);
		}

		if (negativo) {
			resultado = "-" + resultado;
		}

		return resultado;
	}

	public static GregorianCalendar zerarHora(GregorianCalendar dataCompleta) {

		int ano = dataCompleta.get(GregorianCalendar.YEAR);
		int mes = dataCompleta.get(GregorianCalendar.MONTH);
		int diaDoMes = dataCompleta.get(GregorianCalendar.DAY_OF_MONTH);

		return new GregorianCalendar(ano, mes, diaDoMes);
	}

	/**
	 * Calcula a diferen�a em dias entre as duas datas, ou seja ignorando as
	 * horas. Ex: 15/06 as 15:00 at� 16/06 as 2:00 a diferen�a � 1 dia
	 * 
	 * @param dataInicio
	 * @param dataFim
	 */
	public static Integer diferencaDias(GregorianCalendar dataInicio, GregorianCalendar dataFim) {
		GregorianCalendar dataInicioSemHora = UtilData.zerarHora(dataInicio);
		GregorianCalendar dataFimSemHora = UtilData.zerarHora(dataFim);

		return UtilData.calculaDuracaoDias(dataInicioSemHora, dataFimSemHora);
	}

	/**
	 * Retorna uma data sem hora, minuto e segundo
	 * 
	 * @param dataHora
	 * @return
	 */
	public static GregorianCalendar retornaDataSemHora(final GregorianCalendar dataHora) {

		return new GregorianCalendar(dataHora.get(GregorianCalendar.YEAR), dataHora.get(GregorianCalendar.MONTH), dataHora.get(GregorianCalendar.DAY_OF_MONTH));
	}

	/**
	 * Converte dias para horas.
	 * 
	 * Ex: 2 dias � igual a 48:00:00 horas
	 * 
	 * Ex: 2,5 dias � igual a 60:00:00 horas
	 */
	public static String converteDiasParaHoras(BigDecimal dias) {

		Integer segundos = Integer.valueOf(UtilNumero.formatarNumeroSemPonto(UtilNumero.arredondaParaInteiroMaisProximo(UtilNumero.multiplica(dias, new BigDecimal(86400)))));

		return converteSegundosParaMascaraApenasComHorasEMinutos(segundos);
	}

	/**
	 * Converte os segundos passados em dias
	 * 
	 * Ex: 216000 segundos � igual a 2 dias e meio
	 */
	public static BigDecimal converteSegundosParaDias(Integer segundos) {

		return UtilNumero.divide(UtilNumero.converterParaBigDecimal(segundos), new BigDecimal(86400));
	}
	
	/**
	 * Converte os segundos passados em horas
	 */
	public static BigDecimal converteSegundosParaHoras(Integer segundos) {
		
		return UtilNumero.divide(UtilNumero.converterParaBigDecimal(segundos), new BigDecimal(3600));
	}

	/**
	 * Retorna o �ltimo dia do m�s da data passada
	 */
	public static int obterUltimoDiaDoMes(GregorianCalendar data) {

		GregorianCalendar data2 = new GregorianCalendar(data.get(GregorianCalendar.YEAR), data.get(GregorianCalendar.MONTH), data.get(GregorianCalendar.DAY_OF_MONTH));

		int mes = data2.get(GregorianCalendar.MONTH);
		int ano = data2.get(GregorianCalendar.YEAR);

		data2.set(ano, mes + 1, 1);

		data2 = subtraiDias(1, data2);

		return data2.get(GregorianCalendar.DAY_OF_MONTH);

	}

	/**
	 * Obt�m o dia da semana da data passada como par�metro
	 */
	public static String getDiaSemana(GregorianCalendar data) {

		HashMap<Integer, String> dias = new HashMap<Integer, String>();
		dias.put(GregorianCalendar.SUNDAY, "Dom");
		dias.put(GregorianCalendar.MONDAY, "Seg");
		dias.put(GregorianCalendar.TUESDAY, "Ter");
		dias.put(GregorianCalendar.WEDNESDAY, "Qua");
		dias.put(GregorianCalendar.THURSDAY, "Qui");
		dias.put(GregorianCalendar.FRIDAY, "Sex");
		dias.put(GregorianCalendar.SATURDAY, "S�b");

		return dias.get(data.get(GregorianCalendar.DAY_OF_WEEK));

	}
}