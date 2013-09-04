package br.com.hackathon.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilString {

    private static final String[] SIMBOLOS = { ",", "." };

    /**
     * ATEN��O: S� retorna n�meros, se houver alguma string ela voltar� como ""
     * 
     * @param string
     * @param delimitador
     * @return
     */
    public static String[] split(String string, String delimitador) {

        int numeroDelimitadores = UtilString.numeroDelimitadores(string, delimitador);
        String[] resultado = new String[numeroDelimitadores + 1];

        for(int i = 0, j = 0; i < string.length(); i++) {

            if(!string.substring(i, i + 1).equals(delimitador)) {

                if(resultado[j] == null) {
                    resultado[j] = string.substring(i, i + 1);
                } else {
                    resultado[j] += string.substring(i, i + 1);
                }
            } else {
                j++;
            }

        }

        for(int i = 0; i < resultado.length; i++) {
            if(!UtilNumero.isInt(resultado[i])) {
                resultado[i] = "";
            }
        }

        return resultado;
    }

    /**
     * Faz split numa string
     * 
     * @param string que deve ser separada
     * @param delimitador
     * @return array de string com elementos delimitados pelo par�metro delimitador
     */
    public static String[] splitQualquerCoisa(String string, String delimitador) {

        int numeroDelimitadores = UtilString.numeroDelimitadores(string, delimitador);
        String[] resultado = new String[numeroDelimitadores + 1];

        for(int i = 0, j = 0; i < string.length(); i++) {

            if(!string.substring(i, i + 1).equals(delimitador)) {

                if(resultado[j] == null) {
                    resultado[j] = string.substring(i, i + 1);
                } else {
                    resultado[j] += string.substring(i, i + 1);
                }
            } else {
                j++;
            }

        }
        return resultado;
    }

    /**
     * Informa o n�mero de delimitadores presentes em uma string
     * 
     * @param string String que ser� analisada
     * @param delimitador String delimitadora que ser� procurada na "string"
     * @return n�mero de delimitadores presentes em uma string
     */
    public static int numeroDelimitadores(String string, String delimitador) {

        int resultado = 0;

        for(int i = 0; i < string.length(); i++) {
            if(string.substring(i, i + 1).equals(delimitador)) {
                resultado++;
            }
        }

        return resultado;
    }

    /**
     * Verifica se "caracter" aparece uma �nica vez em "string"
     * 
     * @param string
     * @param caracter
     * @return
     */
    public static boolean apareceNoMaximoUmaVez(String string, String caracter) {

        boolean temUm = false;

        for(int i = 0; i < string.length(); i++) {

            // se o caracter for o procurado
            if(String.valueOf(string.charAt(i)).equals(caracter)) {
                if(!temUm) {
                    // nao tinha nada, agora tem um
                    temUm = true;
                } else {
                    // tem mais de um
                    return false;
                }
            }
        }

        return true;
    }

    public static String trimToNull(String campo) {

        if(Util.preenchido(campo)) {
            return campo.trim();
        } else {
            return null;
        }
    }

    public static String doubleQuote(String string) {
        return "\"" + string + "\"";
    }

    public static String singleQuote(String conteudo) {
        return "'" + conteudo + "'";
    }

    /**
     * Troca todos os pontos (".") de uma string por v�rgula (",").
     * 
     * @param string String que ser� alterada
     * @return String alterada
     */
    public static String trocarPontoPorVirgula(Object object) {
        return String.valueOf(object).replace('.', ',');
    }

    /**
     * Troca todas as v�rgulas (",") de uma string por pontos (".").
     * 
     * @param string String que ser� alterada
     * @return String alterada
     */
    public static String trocarVirgulaPorPonto(Object object) {
        return String.valueOf(object).replace(',', '.');
    }

    /**
     * Verifica se uma string segue um formato
     * 
     * @param string String a ser verificada
     * @param formato Formato que deve ser seguido
     * @return "True" caso a string siga o formato e "false" caso contr�rio.
     */
    public static boolean verificarFormato(String string, String formato) {
    	
        Pattern padrao = Pattern.compile(formato);

        Matcher pesquisa = padrao.matcher(string);

        if(pesquisa.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retira todos os s�mbolos de uma String
     * 
     * @param numero
     * @return
     */
    public static String retirarSimbolos(String numero) {

        // retira todos os s�mbolos
        for(String simbolo : SIMBOLOS) {
            numero = numero.replace(simbolo, "");
        }

        return numero;
    }

    /**
     * Retira todos os 0's (zeros) � esquerda da String
     * 
     * @param string String que ter� os 0's � esquerda retirados
     * @return A string passada como par�metro sem zeros � esquerda
     */
    public static String retiraZeroEsquerda(String string) {

        for(;;) {
            if(Util.preenchido(string) && String.valueOf(string.charAt(0)).equals("0")) {
                string = string.substring(1);
            } else {
                break;
            }
        }

        return string;
    }

    /**
     * Retira todos os 0's (zeros) � direita da String
     * 
     * @param string String que ter� os 0's � direita retirados
     * @return A string passada como par�metro sem zeros � direita
     */
    public static String retiraZeroDireita(String string) {

        for(;;) {
            if(Util.preenchido(string) && String.valueOf(string.charAt(string.length() - 1)).equals("0")) {
                // retira o primeiro caracter, ou seja, o "0"
                string = string.substring(0, string.length() - 1);
            } else {
                break;
            }
        }

        return string;
    }

    private static final int LIMITE = 100;
    
   
    public static String formataTexto(String texto, int limite, int corte) {
        
        String novoTexto = "";
        String[] linha;
        
        if(Util.vazio(texto)) {
            texto = "&nbsp";
        }
 
        if(limite == 0) {
            limite = LIMITE;
        }
        
        linha = texto.split("\n");
        
        for(int i = 0; i < linha.length; i++) {
            
            if(linha[i].length() > limite) {
                novoTexto += quebraLinha(linha[i], limite);
            } else {
                novoTexto += linha[i];
            }
        }
        
        novoTexto = adicionaStringCorte(corte, novoTexto);
        
        novoTexto = substituiCodigoHTML(novoTexto);
        
        
        return novoTexto;
    }

    public static String substituiCodigoHTML(String novoTexto) {
        
        novoTexto = novoTexto.replace("=","&#61;");
        novoTexto = novoTexto.replace("<","&#60;");
        novoTexto = novoTexto.replace(">","&#62;");
        novoTexto = novoTexto.replace(".","&#46;");
        novoTexto = novoTexto.replace("(","&#40;");
        novoTexto = novoTexto.replace(")","&#41;");
        novoTexto = novoTexto.replace("-","&#45;");
        novoTexto = novoTexto.replace("\"","&#34;");
        novoTexto = novoTexto.replace("'","&#39;");
        
        return novoTexto;
    }

    private static String adicionaStringCorte(int corte, String novoTexto) {
        if(corte > 0 && novoTexto.length() > corte) {
            novoTexto = novoTexto.substring(0,corte);
            novoTexto += " [...]";
        }
        return novoTexto;
    }
    
    private static String quebraLinha(String linha, int limite) {

        String velha;
        String nova;
        int indice = limite;
        
        if(linha.length() > limite) {
            
            while(indice > 0) {
                
                if(linha.charAt(indice) == ' ') {
                    
                    velha = linha.substring(0,indice) + "\n";
                    nova = linha.substring(indice+1);
                    linha = velha + quebraLinha(nova,limite);
                    break;
                }
                indice --;
            }
        }
        
        return linha;
    }

    /** Recebe a mensagem que deve ser enviada por email e adiciona a string de quebra de linha (<br>) no lugar do '/n'.
     * N�o permite c�digo HTML no conte�do da mensagem*/
    public static String formataMensagemEmailSubstituindoHTML(String texto) {
            
        String[] linha = texto.split("\n");
        String novaMensagem = "";
        
        for(String string: linha){
           novaMensagem +=  UtilString.substituiCodigoHTML(string) + " <br>";
        }

        return novaMensagem;
    }
    

    /**
     * retorna true se a string passada � um "on"
     * @param str
     * @return
     */
    public static boolean traduzBooleano(String str){
        if(str.equalsIgnoreCase("on")){
            return true;
        }
        return false;
    }
    
    
    /**
     * Adiciona zeros a esquerda do texto at� ele chegar ao tamanho desej�vel
     * @param texto
     * @param tamanhoDesejavel
     * @return
     */
    public static String preencheComZerosEsquerda(final String texto, final Integer tamanhoDesejavel) {
        
        final StringBuffer resultado = new StringBuffer(texto);
        
        while(resultado.length() < tamanhoDesejavel) {
            
            resultado.insert(0, 0);
        }
        
        return resultado.toString();
    }
    
    /**
     * retorna o caracter delimitador da string.
     * @param string
     * @return
     */
    public static String retornaCaracterDelimitador(String string) {
    	
    	String delimitador = string.replaceAll("[0-9A-Za-z]+", "");
    	
    	if(delimitador.length() > 0) {
    		return String.valueOf(delimitador.charAt(0));
    	} else {
    		return null;
    	}
    }
    
    /**
     * remove a ultima substring depois do ultimo delimitador.
     * @param string
     * @param delimitador
     * @return
     */
    public static String removeSubStringAteUltimoDelimitador(String string, String delimitador) {
		
		for(int i = string.length()-1; i >0; i--) {

            if(string.substring(i, i + 1).equals(delimitador)) {

                return(string.substring(0, i));
                         
            }
        }
		
		return "";
	}

	/**
	 * Multiplica uma string por um inteiro.
	 * Retorna a mesma string n vezes.
	 * @param s
	 * @param n
	 * @return
	 */
	public static String multiplica(String s, Integer n) {
		final StringBuffer resultado = new StringBuffer();
		while (n > 0){
			resultado.append(s);
			n--;
		}
		
		return resultado.toString();
	}
}
