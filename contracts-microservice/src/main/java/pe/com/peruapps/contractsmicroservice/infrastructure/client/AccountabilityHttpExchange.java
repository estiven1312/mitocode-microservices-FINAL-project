package pe.com.peruapps.contractsmicroservice.infrastructure.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import pe.com.peruapps.contractsmicroservice.infrastructure.client.dto.CreatePaymentRequest;
import pe.com.peruapps.contractsmicroservice.infrastructure.client.dto.CreatePaymentResponse;

@HttpExchange("/api/v1")
public interface AccountabilityHttpExchange {
  // Define methods for HTTP requests to the accountability service here
  @GetExchange("/payments/{code}/validate")
  AccountabilityPaymentResponse validatePayment(@PathVariable String code);

  @PostExchange("/payments")
  CreatePaymentResponse createPayment(@RequestBody CreatePaymentRequest request);
}
