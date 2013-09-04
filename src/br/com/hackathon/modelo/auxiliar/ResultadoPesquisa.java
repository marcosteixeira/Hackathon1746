package br.com.hackathon.modelo.auxiliar;

import java.util.ArrayList;
import java.util.List;


public class ResultadoPesquisa<E extends Object>{
    
    private List<E> resultado;
    private Integer qtdeRegistros;
    
    public ResultadoPesquisa() {
		this.resultado = new ArrayList<E>();
		this.qtdeRegistros = Integer.valueOf(0);
	}
    
    public List<E> getResultado() {
        return resultado;
    }
    
    public void setResultado(List<E> resultado) {
        this.resultado = resultado;
    }
    
    public Integer getQtdeRegistros() {
        return qtdeRegistros;
    }
    
    public void setQtdeRegistros(Integer qtdeRegistros) {
        this.qtdeRegistros = qtdeRegistros;
    }
}
