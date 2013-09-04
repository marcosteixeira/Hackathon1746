package br.com.hackathon.modelo.entidade;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.gson.annotations.Expose;

@Entity
public class Usuario implements Entidade, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8366894918036605426L;
	
	public static final String LABEL_SEXO_MASCULINO = "male";
	public static final Integer VALUE_SEXO_MASCULINO = 1;
	public static final Integer VALUE_SEXO_FEMININO = 2;
	public static final String ATRIBUTO_EMAIL = "email";
	public static final String ADMINISTRADOR = "administrador";
	
	@Id
	@GeneratedValue
	@Expose
	private Integer id;
	@Expose
	private String nome;
	private String telefone;
	private String celular;
	private String email;
	private String senha;
	private String idFacebook;
	private String idTwitter;
	private GregorianCalendar dataCadastro;
	private Endereco endereco;
	
	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<HistoricoLogin> historicoLogin;

	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Ocorrencia> ocorrencias;
	
	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Assinatura> assinaturas;

	public List<Ocorrencia> getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(List<Ocorrencia> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	public List<Assinatura> getAssinaturas() {
		return assinaturas;
	}

	public void setAssinaturas(List<Assinatura> assinaturas) {
		this.assinaturas = assinaturas;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Usuario() {}

	public Usuario(String email) {
		this.email = email ;
	}

	public Usuario(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAdministrador() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setId(Integer id) {
		this.id = id;
		
	}

	public Integer getId() {
		return this.id;
	}

	public GregorianCalendar getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(GregorianCalendar dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIdFacebook() {
		return idFacebook;
	}

	public void setIdFacebook(String idFacebook) {
		this.idFacebook = idFacebook;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getIdTwitter() {
		return idTwitter;
	}

	public void setIdTwitter(String idTwitter) {
		this.idTwitter = idTwitter;
	}
	
	public List<HistoricoLogin> getHistoricoLogin() {
		return historicoLogin;
	}

	public void setHistoricoLogin(List<HistoricoLogin> historicoLogin) {
		this.historicoLogin = historicoLogin;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereço(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}
	
}
