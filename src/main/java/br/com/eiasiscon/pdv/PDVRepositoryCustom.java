package br.com.eiasiscon.pdv;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PDVRepositoryCustom {	
	
	Page<PDV> find(String q, String empresa, Pageable page);
}
