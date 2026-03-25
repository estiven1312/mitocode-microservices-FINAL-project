package pe.com.peruapps.esignaturemicroservice.infrastructure.out.persistence;

import pe.com.peruapps.esignaturemicroservice.domain.model.Id;
import pe.com.peruapps.esignaturemicroservice.domain.model.Signature;

import java.util.UUID;

public final class SignatureMongoMapper {

  private SignatureMongoMapper() {}

  public static SignatureMongoDocument toDocument(Signature signature) {
    return SignatureMongoDocument.builder()
        .id(signature.getId().value().toString())
        .contractId(signature.getContractId().value())
        .urlDocumentSigned(signature.getUrlDocumentSigned())
        .hashDocument(signature.getHashDocument())
        .signedAt(signature.getSignedAt())
        .status(signature.getStatus())
        .codeSignature(signature.getCodeSignature())
        .urlSignature(signature.getUrlSignature())
        .hashSignature(signature.getHashSignature())
        .signatureCreatedAt(signature.getSignatureCreatedAt())
        .details(signature.getDetails())
        .signerId(signature.getSignerId() == null ? null : signature.getSignerId().value())
        .build();
  }

  public static Signature toDomain(SignatureMongoDocument document) {
    return Signature.builder()
        .id(new Id(UUID.fromString(document.getId())))
        .contractId(new Id(document.getContractId()))
        .urlDocumentSigned(document.getUrlDocumentSigned())
        .hashDocument(document.getHashDocument())
        .signedAt(document.getSignedAt())
        .status(document.getStatus())
        .codeSignature(document.getCodeSignature())
        .urlSignature(document.getUrlSignature())
        .hashSignature(document.getHashSignature())
        .signatureCreatedAt(document.getSignatureCreatedAt())
        .details(document.getDetails())
        .signerId(document.getSignerId() == null ? null : new Id(document.getSignerId()))
        .build();
  }
}
