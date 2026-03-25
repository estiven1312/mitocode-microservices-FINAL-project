package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.LinkDeliverablesRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.MilestoneResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

public interface LinkDeliverablesUseCase {
  MilestoneResponse execute(Contract.ContractId contractId, LinkDeliverablesRequest request);
}
