package br.com.hackathon.modelo.entidade;

import java.io.Serializable;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.com.hackathon.modelo.entidade.Entidade;
import br.com.hackathon.modelo.entidade.Usuario;

@Entity
public class HistoricoLogin implements Entidade, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8491109355202553028L;
	public static final String DATA_LOGOUT = "dataLogout";
	public static final String DATA_ACESSO = "dataAcesso";
	@Id
	@GeneratedValue
	private Integer id;
	@ManyToOne(optional=false)
	private Usuario usuario;
	
	@Column(nullable=false)
	private GregorianCalendar dataAcesso;
	
	private GregorianCalendar dataLogout;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public GregorianCalendar getDataAcesso() {
		return dataAcesso;
	}

	public void setDataAcesso(GregorianCalendar dataAcesso) {
		this.dataAcesso = dataAcesso;
	}


	public GregorianCalendar getDataLogout() {
		return dataLogout;
	}

	public void setDataLogout(GregorianCalendar dataLogout) {
		this.dataLogout = dataLogout;
	}

	
}
