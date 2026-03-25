package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.AddDeliverableRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.DeliverableResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

public interface AddDeliverableUseCase {
  DeliverableResponse execute(Contract.ContractId contractId, AddDeliverableRequest request);
}
