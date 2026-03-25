package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.ObligationResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.Obligation;

public interface GetObligationUseCase {
  ObligationResponse execute(Contract.ContractId contractId, Obligation.ObligationId obligationId);
}
