package pe.com.peruapps.contractsmicroservice.infrastructure.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos;
import pe.com.peruapps.contractsmicroservice.application.port.AccountabilityPort;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.infrastructure.client.dto.CreatePaymentRequest;
import pe.com.peruapps.contractsmicroservice.infrastructure.client.dto.CreatePaymentResponse;

/**
 * HTTP adapter that implements the AccountabilityClient outbound port. Calls the Accountability
 * microservice to validate a payment code.
 */
@Component
@RequiredArgsConstructor
public class AccountabilityPortAdapter implements AccountabilityPort {

  private final AccountabilityHttpExchange accountabilityHttpExchange;

  @Override
  @Retry(name = "checkPaymentRetry", fallbackMethod = "validatePaymentFallback")
  public AccountabilityPaymentResult validatePayment(String codePayment) {

    AccountabilityPaymentResponse response =
        accountabilityHttpExchange.validatePayment(codePayment);

    if (response == null) {
      return new AccountabilityPaymentResult(
          codePayment, false, "No response received from Accountability service.");
    }

    return new AccountabilityPaymentResult(
        response.codePayment(), response.valid(), response.message());
  }

  public AccountabilityPaymentResult validatePaymentFallback(String codePayment, Throwable ex) {
    return new AccountabilityPaymentResult(
        codePayment, false, "Accountability service is currently unavailable: " + ex.getMessage());
  }

  @CircuitBreaker(name = "createPaymentCircuitBreaker", fallbackMethod = "createPaymentFallback")
  @Override
  public CreatePaymentResponse createPayment(
      Contract.ContractId id, ContractPerformanceDtos.AddPaymentRequest request) {

    CreatePaymentRequest httpRequest =
        new CreatePaymentRequest(
            request.codePayment(),
            "Contract-" + id.value() + " Payment: " + request.codePayment(),
            request.currency(),
            request.amount());

    return accountabilityHttpExchange.createPayment(httpRequest);
  }

  // Fallback method for createPayment circuit breaker - in this case, we choose to throw an
  // exception to indicate the failure
  public CreatePaymentResponse createPaymentFallback(
      Contract.ContractId id, ContractPerformanceDtos.AddPaymentRequest request, Throwable ex) {
    throw new RuntimeException(ex);
  }
}
