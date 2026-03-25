package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.MilestoneResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

import java.util.List;

public interface GetMilestonesUseCase {
  List<MilestoneResponse> execute(Contract.ContractId contractId);
}
