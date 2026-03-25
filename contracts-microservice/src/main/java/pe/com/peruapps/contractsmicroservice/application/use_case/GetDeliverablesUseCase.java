package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.DeliverableResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

import java.util.List;

public interface GetDeliverablesUseCase {
  List<DeliverableResponse> execute(Contract.ContractId contractId);
}
