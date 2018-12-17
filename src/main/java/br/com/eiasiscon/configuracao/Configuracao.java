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

}
