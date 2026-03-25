package pe.com.peruapps.esignaturemicroservice.infrastructure.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface SignatureMongoRepository extends MongoRepository<SignatureMongoDocument, UUID> {

  Optional<SignatureMongoDocument> findByContractId(UUID contractId);

  void deleteByContractId(UUID contractId);
}
