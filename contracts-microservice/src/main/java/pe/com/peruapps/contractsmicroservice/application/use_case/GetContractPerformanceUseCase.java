package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.PerformanceResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

public interface GetContractPerformanceUseCase {
  PerformanceResponse execute(Contract.ContractId contractId);
}
