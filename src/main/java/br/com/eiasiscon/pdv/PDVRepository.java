package br.com.eiasiscon.pdv;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDVRepository extends MongoRepository<PDV, String>, PDVRepositoryCustom {
}
