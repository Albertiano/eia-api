package br.com.eiasiscon.contato;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import br.com.eiasiscon.contato.Contato;

@Repository
public class ContatoRepositoryImpl implements ContatoRepositoryCustom{
	
	private final MongoOperations operations;

	@Autowired
	public ContatoRepositoryImpl(MongoOperations operations) {

		Assert.notNull(operations, "[Assertion failed] - this argument is required; it must not be null");
		this.operations = operations;
	}

	@Override
	public Page<Contato> find(String q, String empresa, Pageable pageable) {
		Query query = new Query();
		
		query.addCriteria(
				Criteria.where("empresa.id").is(empresa)
				.orOperator(
							Criteria.where("nome").regex(q,"i"), 
							Criteria.where("contato").regex(q,"i"),
							Criteria.where("fantasia").regex(q,"i"),
							Criteria.where("numDoc").regex(q,"i"),
							Criteria.where("bairro").regex(q,"i")
				)
		);
		
		query.with(pageable);
		
		query.fields().exclude("empresa");
		
		List<Contato> list = operations.find(query, Contato.class);
	    return PageableExecutionUtils.getPage(list, pageable,
	            () -> operations.count(query, Contato.class));
	}

}
