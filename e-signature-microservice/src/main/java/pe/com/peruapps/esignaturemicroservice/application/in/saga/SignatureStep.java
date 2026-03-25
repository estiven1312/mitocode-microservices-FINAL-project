package pe.com.peruapps.esignaturemicroservice.application.in.saga;

public interface SignatureStep {

  SignatureSagaContext execute(SignatureSagaContext context);

  void compensate(SignatureSagaContext context);

  record SignatureData(String contractId, String signerEmail) {}
}
