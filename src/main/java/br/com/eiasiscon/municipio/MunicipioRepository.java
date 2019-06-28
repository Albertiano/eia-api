package br.com.eiasiscon.municipio;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.eiasiscon.municipio.Municipio;


public interface MunicipioRepository extends MongoRepository<Municipio, String>, MunicipioRepositoryCustom {
	
}
