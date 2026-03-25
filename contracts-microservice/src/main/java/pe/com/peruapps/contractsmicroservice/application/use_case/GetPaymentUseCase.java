package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.PaymentResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.Payment;

public interface GetPaymentUseCase {
  PaymentResponse execute(Contract.ContractId contractId, Payment.PaymentId paymentId);
}
