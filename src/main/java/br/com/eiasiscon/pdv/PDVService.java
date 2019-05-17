package br.com.eiasiscon.pdv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.eiasiscon.base.BaseService;

@Service
public class PDVService extends BaseService<PDV, String> {
	
	@Autowired
	private PDVRepository repository;
	
	@Autowired
	public void setJpaRepository(PDVRepository jpa) {
		setJpa(jpa);
	}
	
	public Page<PDV> find(String query, String empresa, Pageable pageable) {
		Page<PDV>  entities = repository.find(query, empresa, pageable);
		return entities;
	}
}

