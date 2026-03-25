package pe.com.peruapps.esignaturemicroservice.infrastructure.in.web;

public record SignContractRequest(
    String urlDocument,
    String email,
    String signerName,
    String signerLastName,
    String signerDni,
    String signerPhone,
    String signerEmail) {}

