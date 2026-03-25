package pe.com.peruapps.esignaturemicroservice.infrastructure.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.peruapps.esignaturemicroservice.application.out.SignaturePersistencePort;
import pe.com.peruapps.esignaturemicroservice.domain.errors.SignatureNotFoundException;
import pe.com.peruapps.esignaturemicroservice.domain.model.Id;
import pe.com.peruapps.esignaturemicroservice.domain.model.Signature;

@Component
@RequiredArgsConstructor
public class SignaturePersistenceAdapter implements SignaturePersistencePort {

  private final SignatureMongoRepository signatureMongoRepository;

  @Override
  public Signature save(Signature signature) {
    var saved = signatureMongoRepository.save(SignatureMongoMapper.toDocument(signature));
    return SignatureMongoMapper.toDomain(saved);
  }

  @Override
  public Signature findByContractId(Id contractId) {
    return signatureMongoRepository
        .findByContractId(contractId.value())
        .map(SignatureMongoMapper::toDomain)
        .orElseThrow(
            () ->
                new SignatureNotFoundException(
                    contractId.value().toString(),
                    "Signature not found for contract id: " + contractId.value()));
  }

  @Override
  public void deleteByContractId(Id contractId) {
    signatureMongoRepository.deleteByContractId(contractId.value());
  }
}
