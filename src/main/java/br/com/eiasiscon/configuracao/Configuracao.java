package br.com.eiasiscon.configuracao;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.eiasiscon.base.BaseEntity;
import br.com.eiasiscon.empresa.Empresa;

@Document
public class Configuracao extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@DBRef
    private Empresa empresa;
	
	private String certificadoFile;
	private String certificadoSenha;
	private String logoFile;
	private Long serieNFe;
	private Long numeroNFe;
	private String idCsc;
	private String csc;
	private Long serieNFCe;
	private Long numeroNFCe;
	
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public String getCertificadoFile() {
		return certificadoFile;
	}
	public void setCertificadoFile(String certificadoFile) {
		this.certificadoFile = certificadoFile;
	}
	public String getCertificadoSenha() {
		return certificadoSenha;
	}
	public void setCertificadoSenha(String certificadoSenha) {
		this.certificadoSenha = certificadoSenha;
	}
	public String getLogoFile() {
		return logoFile;
	}
	public void setLogoFile(String logoFile) {
		this.logoFile = logoFile;
	}
	public Long getSerieNFe() {
		return serieNFe;
	}
	public void setSerieNFe(Long serieNFe) {
		this.serieNFe = serieNFe;
	}
	public Long getNumeroNFe() {
		return numeroNFe;
	}
	public void setNumeroNFe(Long numeroNFe) {
		this.numeroNFe = numeroNFe;
	}
	public String getIdCsc() {
		return idCsc;
	}
	public void setIdCsc(String idCsc) {
		this.idCsc = idCsc;
	}
	public String getCsc() {
		return csc;
	}
	public void setCsc(String csc) {
		this.csc = csc;
	}
	public Long getSerieNFCe() {
		return serieNFCe;
	}
	public void setSerieNFCe(Long serieNFCe) {
		this.serieNFCe = serieNFCe;
	}
	public Long getNumeroNFCe() {
		return numeroNFCe;
	}
	public void setNumeroNFCe(Long numeroNFCe) {
		this.numeroNFCe = numeroNFCe;
	}

}
