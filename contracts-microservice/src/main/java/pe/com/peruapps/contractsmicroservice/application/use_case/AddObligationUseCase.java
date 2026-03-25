package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.AddObligationRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.ObligationResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

public interface AddObligationUseCase {
  ObligationResponse execute(Contract.ContractId contractId, AddObligationRequest request);
}
