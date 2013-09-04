package br.com.hackathon.modelo.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Assinatura implements Entidade, Serializable {

	private static final long serialVersionUID = -3443506706867729806L;
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@ManyToOne(optional=true)
	private Usuario usuario;
	
	@Column(nullable=false)
	private String email;
	
	@ManyToOne(optional=false)
	private Ocorrencia ocorrencia;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}
	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	

}