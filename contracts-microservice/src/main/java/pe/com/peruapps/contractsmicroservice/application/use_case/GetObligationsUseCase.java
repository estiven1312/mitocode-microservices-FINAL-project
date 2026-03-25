package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.ObligationResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

import java.util.List;

public interface GetObligationsUseCase {
  List<ObligationResponse> execute(Contract.ContractId contractId);
}
