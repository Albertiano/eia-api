package br.com.eiasiscon.pais;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.eiasiscon.pais.Pais;

public interface PaisRepository extends MongoRepository<Pais, String> {

}
