package br.com.eiasiscon.financeiro.centrocustos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CentroCustosRepositoryCustom {	
	
	Page<CentroCustos> find(String q, String empresa, Pageable page);
}
