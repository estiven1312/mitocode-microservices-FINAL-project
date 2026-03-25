package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractResponse;
import pe.com.peruapps.contractsmicroservice.application.dto.UpdateContractBasicInfoRequest;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

public interface UpdateContractBasicInfoUseCase {
  ContractResponse execute(Contract.ContractId contractId, UpdateContractBasicInfoRequest request);
}

