package pe.com.peruapps.esignaturemicroservice.application.in.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import pe.com.peruapps.esignaturemicroservice.application.out.SignaturePersistencePort;
import pe.com.peruapps.esignaturemicroservice.domain.model.Id;
import pe.com.peruapps.esignaturemicroservice.domain.model.Signature;

import java.time.LocalDateTime;

@Service
@Slf4j
@Order(400)
@RequiredArgsConstructor
public class PersistSignatureStep implements SignatureStep {

  private final SignaturePersistencePort signaturePersistencePort;

  @Override
  public SignatureSagaContext execute(SignatureSagaContext context) {
    if (context.urlSignedFile() == null || context.hashSigned() == null) {
      throw new IllegalStateException("Signed file metadata is required to persist signature");
    }

    var signature =
        Signature.builder()
            .id(Id.generate())
            .contractId(context.contractId())
            .urlDocumentSigned(context.urlSignedFile())
            .hashDocument(context.hashSigned())
            .status(Signature.SignatureStatus.SIGNED)
            .signedAt(LocalDateTime.now())
            .build();

    signaturePersistencePort.save(signature);
    log.info("Signature persisted for contractId: {}", context.contractId().value());
    return context;
  }

  @Override
  public void compensate(SignatureSagaContext context) {
    signaturePersistencePort.deleteByContractId(context.contractId());
    log.error("Signature deleted by compensation for contractId: {}", context.contractId().value());
  }
}

