package pe.com.peruapps.esignaturemicroservice.application.in.saga;

import pe.com.peruapps.esignaturemicroservice.domain.model.Id;


public record SignatureSagaContext(
    Id contractId,
    String urlDocument,
    String email,
    String signerName,
    String signerLastName,
    String signerDni,
    String signerPhone,
    String signerEmail,
    String urlSignedFile,
    String hashSigned
) {

  public SignatureSagaContext withSignedMetadata(String newUrlSignedFile, String newHashSigned) {
    return new SignatureSagaContext(
        contractId,
        urlDocument,
        email,
        signerName,
        signerLastName,
        signerDni,
        signerPhone,
        signerEmail,
        newUrlSignedFile,
        newHashSigned);
  }
}
