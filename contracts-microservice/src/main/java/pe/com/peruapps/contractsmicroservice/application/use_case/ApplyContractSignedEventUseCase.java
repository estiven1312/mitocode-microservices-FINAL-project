package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

public interface ApplyContractSignedEventUseCase {
  void execute(Contract.ContractId contractId, String urlSignedFile, String hashSigned);
}

