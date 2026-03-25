package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.AddPaymentRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.PaymentResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

public interface AddPaymentUseCase {
  PaymentResponse execute(Contract.ContractId contractId, AddPaymentRequest request);
}
