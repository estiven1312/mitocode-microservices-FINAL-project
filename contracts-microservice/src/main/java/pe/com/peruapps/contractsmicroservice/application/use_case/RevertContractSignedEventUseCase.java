package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

public interface RevertContractSignedEventUseCase {
  void execute(Contract.ContractId contractId);
}

