package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.MarkPaymentAsPaidRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.PaymentResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

public interface MarkPaymentAsPaidUseCase {
  PaymentResponse execute(Contract.ContractId contractId, MarkPaymentAsPaidRequest request);
}
