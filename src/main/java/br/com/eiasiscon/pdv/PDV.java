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
	
}
