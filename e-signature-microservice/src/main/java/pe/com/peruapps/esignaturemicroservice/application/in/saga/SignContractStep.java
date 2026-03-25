package pe.com.peruapps.esignaturemicroservice.application.in.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import pe.com.peruapps.esignaturemicroservice.application.out.SignatureProviderPort;

@Service
@Slf4j
@Order(200)
@RequiredArgsConstructor
public class SignContractStep implements SignatureStep {

  private final SignatureProviderPort signatureProviderPort;

  @Override
  public SignatureSagaContext execute(SignatureSagaContext context) {
    var response =
        signatureProviderPort.requestSignature(
            new SignatureProviderPort.SignatureCommand(
                context.contractId(),
                context.urlDocument(),
                context.email(),
                context.signerName(),
                context.signerLastName(),
                context.signerDni(),
                context.signerPhone(),
                context.signerEmail()));

    if (response.status() != SignatureProviderPort.ESignatureStatus.RESOLVED
        || response.urlDocumentSigned() == null
        || response.hashSignature() == null) {
      throw new IllegalStateException("Signature provider did not return a valid signed document");
    }

    log.info("Contract signed successfully for contractId: {}", context.contractId().value());
    return context.withSignedMetadata(response.urlDocumentSigned(), response.hashSignature());
  }

  @Override
  public void compensate(SignatureSagaContext context) {
    log.error("Compensating SignContractStep for contractId: {}", context.contractId().value());
  }
}
