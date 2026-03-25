package pe.com.peruapps.esignaturemicroservice.application.out;

import pe.com.peruapps.esignaturemicroservice.domain.model.Id;
import pe.com.peruapps.esignaturemicroservice.domain.model.Signature;

public interface SignaturePersistencePort {

  Signature save(Signature signature);

  Signature findByContractId(Id contractId);

  void deleteByContractId(Id contractId);
}
