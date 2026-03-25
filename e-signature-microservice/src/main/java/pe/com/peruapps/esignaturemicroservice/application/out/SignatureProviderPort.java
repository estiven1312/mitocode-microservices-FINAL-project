package pe.com.peruapps.esignaturemicroservice.application.out;

import pe.com.peruapps.esignaturemicroservice.domain.model.Id;

public interface SignatureProviderPort {

  SignatureResponse requestSignature(SignatureCommand command);

  void deleteSignature(Id contractId);

  record SignatureCommand(
      Id contractId,
      String urlDocument,
      String email,
      String signerName,
      String signerLastName,
      String signerDni,
      String signerPhone,
      String signerEmail) {}

  record SignatureResponse(
      String codeSignature,
      Id contractId,
      ESignatureStatus status,
      String urlDocumentSigned,
      String hashSignature,
      String details) {}

  enum ESignatureStatus {
    RESOLVED,
    REJECTED,
    PENDING
  }
}
