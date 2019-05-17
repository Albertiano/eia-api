package br.com.eiasiscon.pdv;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.eiasiscon.base.BaseEntity;
import br.com.eiasiscon.empresa.Empresa;

@Document
public class PDV extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -4591790684476266712L;
	
	@DBRef
    private Empresa empresa;
	
	private String descricao;
	private Boolean isExterno;
	private Long serieNFe;
	private Long numeroNFe;
	private Long serieNFCe;
	private Long numeroNFCe;
	
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Boolean getIsExterno() {
		return isExterno;
	}
	public void setIsExterno(Boolean isExterno) {
		this.isExterno = isExterno;
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
