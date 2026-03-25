package pe.com.peruapps.contractsmicroservice.application.use_case;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.PaymentResponse;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

import java.util.List;

public interface GetPaymentsUseCase {
  List<PaymentResponse> execute(Contract.ContractId contractId);
}
