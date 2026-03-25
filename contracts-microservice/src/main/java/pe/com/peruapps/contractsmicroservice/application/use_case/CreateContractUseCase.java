package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractResponse;
import pe.com.peruapps.contractsmicroservice.application.dto.CreateContractRequest;

public interface CreateContractUseCase {
  ContractResponse execute(CreateContractRequest request);
}
