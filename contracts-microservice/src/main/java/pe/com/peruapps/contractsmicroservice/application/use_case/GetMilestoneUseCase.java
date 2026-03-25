package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.MilestoneResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.Milestone;

public interface GetMilestoneUseCase {
  MilestoneResponse execute(Contract.ContractId contractId, Milestone.MilestoneId milestoneId);
}
