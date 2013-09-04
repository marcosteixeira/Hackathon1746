package br.com.hackathon.util;



public class UtilContexto {

    /**
     * 
     */
    private static String nomeContexto;
    private static String caminhoCompletoNoDisco;
    private static String baseUrl;

    
    public static String getBaseUrl() {
        return baseUrl;
    }
    
    public static void setBaseUrl(String baseUrl){
    	if(UtilContexto.baseUrl == null){
    		UtilContexto.baseUrl = baseUrl;
    	}
    }
    
    public static String getNomeContexto() {
        return nomeContexto;
    }
    
    public static void setNomeContexto(String nomeContexto) {
        if (UtilContexto.nomeContexto == null){
            UtilContexto.nomeContexto = nomeContexto;
        }
    }

	public static String getCaminhoCompletoNoDisco() {
		return caminhoCompletoNoDisco;
	}

	public static void setCaminhoCompletoNoDisco(String caminhoCompletoNoDisco) {
		UtilContexto.caminhoCompletoNoDisco = caminhoCompletoNoDisco;
	}
	
}
