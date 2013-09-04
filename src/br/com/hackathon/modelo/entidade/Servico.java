package br.com.hackathon.modelo.entidade;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.gson.annotations.Expose;

@Entity
public class Servico implements Entidade, Serializable {

	private static final long serialVersionUID = -3443506706867729806L;
	
	@Id
	@GeneratedValue
	@Expose
	private Integer id;
	@Column(nullable=false)
	@Expose
	private String nome;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="servico" , cascade=CascadeType.ALL)
	private List<Ocorrencia> ocorrencias ;
	
	public Servico(int id) {
		this.id = id;
	}
	public Servico() {
		// TODO Auto-generated constructor stub
	}
	public List<Ocorrencia> getOcorrencias() {
		return ocorrencias;
	}
	public void setOcorrencias(List<Ocorrencia> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

}