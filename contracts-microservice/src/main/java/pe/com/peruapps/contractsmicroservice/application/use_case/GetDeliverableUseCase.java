package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.DeliverableResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.Deliverable;

public interface GetDeliverableUseCase {
  DeliverableResponse execute(Contract.ContractId contractId, Deliverable.DeliverableId deliverableId);
}
