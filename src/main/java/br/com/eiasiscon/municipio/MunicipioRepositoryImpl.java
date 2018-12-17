package br.com.eiasiscon.municipio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import br.com.eiasiscon.municipio.Municipio;
import br.com.eiasiscon.municipio.UF;

@Repository
public class MunicipioRepositoryImpl implements MunicipioRepositoryCustom{

	private final MongoOperations operations;

	@Autowired
	public MunicipioRepositoryImpl(MongoOperations operations) {

		Assert.notNull(operations, "[Assertion failed] - this argument is required; it must not be null");
		this.operations = operations;
	}
	@Override
	public List<Municipio> findByUf(UF uf) {
		Query query = new Query();
		
		query.addCriteria(
				Criteria.where("uf").is(uf)
		);
		
		query.with(Sort.by("xMun"));
		
		List<Municipio> list = operations.find(query, Municipio.class);
	    return list;
	}

}
