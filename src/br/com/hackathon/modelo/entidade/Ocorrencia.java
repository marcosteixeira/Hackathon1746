package br.com.hackathon.modelo.entidade;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import br.com.hackathon.util.UtilContexto;
import com.google.gson.annotations.Expose;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Entity
public class Ocorrencia implements Entidade, Serializable {


	private static final long serialVersionUID = -3443506706867729806L;
	
	@Id
	@GeneratedValue
	@Expose
	private Integer id;
	
	private Integer protocolo;
	@ManyToOne(optional=false)
	@Expose
	private Usuario usuario;
	private GregorianCalendar dataCriacao;
	private String status;
	@Expose
	private String link;
	@Column(columnDefinition="LONGTEXT")
	private String descricao;
	
	@ManyToOne(optional=false)
	@Expose
	private Endereco endereco;
	
	@Expose
	@ManyToOne(optional=false)
	private Servico servico;
	
	@OneToMany(mappedBy="ocorrencia", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Assinatura> assinaturas;

	public List<Assinatura> getAssinaturas() {
		return assinaturas;
	}
	public void setAssinaturas(List<Assinatura> assinaturas) {
		this.assinaturas = assinaturas;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProtocolo() {
		return protocolo;
	}
	public void setProtocolo(Integer protocolo) {
		this.protocolo = protocolo;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public GregorianCalendar getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(GregorianCalendar dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Servico getServico() {
		return servico;
	}
	public void setServico(Servico servico) {
		this.servico = servico;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getQrCode(){
		
		Charset charset = Charset.forName("UTF-8");
	    CharsetEncoder encoder = charset.newEncoder();
	    byte[] b = null;
	    try {
	        // Convert a string to UTF-8 bytes in a ByteBuffer
	        ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(getLinkOcorrencia()));
	        b = bbuf.array();
	    } catch (CharacterCodingException e) {
	        System.out.println(e.getMessage());
	    }

	    String data;
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
	        data = new String(b, "UTF-8");
	        // get a byte matrix for the data
	        BitMatrix matrix = null;
	        int h = 350;
	        int w = 350;
	        com.google.zxing.Writer writer = new MultiFormatWriter();
	        try {
	            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
	            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	            matrix = writer.encode(data,
	            com.google.zxing.BarcodeFormat.QR_CODE, w, h, hints);
	        } catch (com.google.zxing.WriterException e) {
	            System.out.println(e.getMessage());
	        }

	        try {
	            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        }
	        
	    } catch (UnsupportedEncodingException e) {
	        System.out.println(e.getMessage());
	    }
	    return  Base64.encode(baos.toByteArray());

	}

	public String getLinkOcorrencia() {
		return UtilContexto.getBaseUrl()+"/ocorrencia/visualizarOcorrencia/"+this.getLink();
	}
}