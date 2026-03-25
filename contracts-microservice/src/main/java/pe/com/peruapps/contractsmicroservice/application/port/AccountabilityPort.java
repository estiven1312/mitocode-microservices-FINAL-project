package pe.com.peruapps.contractsmicroservice.application.port;

import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.infrastructure.client.dto.CreatePaymentRequest;
import pe.com.peruapps.contractsmicroservice.infrastructure.client.dto.CreatePaymentResponse;

/**
 * Outbound port — calls the Accountability microservice to validate that the given payment code
 * corresponds to a registered payment.
 */
public interface AccountabilityPort {
  AccountabilityPaymentResult validatePayment(String codePayment);

  CreatePaymentResponse createPayment(
      Contract.ContractId contractId, ContractPerformanceDtos.AddPaymentRequest request);

  record AccountabilityPaymentResult(String codePayment, boolean valid, String message) {}
}
