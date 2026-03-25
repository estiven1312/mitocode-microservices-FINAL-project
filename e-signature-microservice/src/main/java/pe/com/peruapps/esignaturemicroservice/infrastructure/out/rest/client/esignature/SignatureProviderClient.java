package pe.com.peruapps.esignaturemicroservice.infrastructure.out.rest.client.esignature;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pe.com.peruapps.esignaturemicroservice.application.out.SignatureProviderPort;
import pe.com.peruapps.esignaturemicroservice.domain.model.Id;

@Service
public class SignatureProviderClient implements SignatureProviderPort {

  private final RestClient eSignatureRestClient;

  public SignatureProviderClient(
      @Qualifier("eSignatureRestClient") RestClient eSignatureRestClient) {
    this.eSignatureRestClient = eSignatureRestClient;
  }

  @Override
  @CircuitBreaker(
      name = "signatureProviderCircuitBreaker",
      fallbackMethod = "fallbackRequestSignature")
  public SignatureResponse requestSignature(SignatureCommand command) {
    ESignatureRequest request =
        new ESignatureRequest(
            command.contractId().value(),
            command.urlDocument(),
            command.email(),
            command.signerName(),
            command.signerLastName(),
            command.signerDni(),
            command.signerPhone(),
            command.signerEmail());

    ESignatureResponse response =
        eSignatureRestClient
            .post()
            .uri("/docu-sign")
            .body(request)
            .retrieve()
            .body(ESignatureResponse.class);
    return new SignatureResponse(
        response.codeSignature(),
        command.contractId(),
        ESignatureStatus.valueOf(response.status()),
        response.urlDocumentSigned(),
        response.hashSignature(),
        response.details());
  }

  @Override
  @Retry(
      name = "signatureDeleteRetry",
      fallbackMethod = "fallbackDeleteSignature")
  public void deleteSignature(Id contractId) {
    eSignatureRestClient
        .delete()
        .uri(
            uriBuilder ->
                uriBuilder.path("/api/v1/docu-sign/{contractCode}").build(contractId.value()))
        .retrieve()
        .toBodilessEntity();
  }

  public SignatureResponse fallbackRequestSignature(SignatureCommand command, Throwable throwable) {
    // Aquí puedes manejar la lógica de fallback, como retornar una respuesta por defecto o lanzar
    // una excepción personalizada
    return new SignatureResponse(
        "fallback-code",
        command.contractId(),
        ESignatureStatus.REJECTED,
        null,
        null,
        "Fallback response due to: " + throwable.getMessage());
  }

  public void fallbackDeleteSignature(Id contractId, Throwable throwable) {
    // Aquí puedes manejar la lógica de fallback para la eliminación, como registrar el error o
    // lanzar
    throw new RuntimeException("Fallback deleteSignature due to: " + throwable.getMessage());
  }
}
