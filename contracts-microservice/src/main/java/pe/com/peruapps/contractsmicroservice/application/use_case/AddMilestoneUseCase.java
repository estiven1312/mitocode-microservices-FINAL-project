package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.AddMilestoneRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.MilestoneResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

public interface AddMilestoneUseCase {
  MilestoneResponse execute(Contract.ContractId contractId, AddMilestoneRequest request);
}
