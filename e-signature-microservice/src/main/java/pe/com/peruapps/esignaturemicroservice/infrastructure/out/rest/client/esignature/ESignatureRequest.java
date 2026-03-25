package pe.com.peruapps.esignaturemicroservice.infrastructure.out.rest.client.esignature;

import java.util.UUID;

public record ESignatureRequest(
    UUID contractId,
    String urlDocument,
    String email,
    String signerName,
    String signerLastName,
    String signerDni,
    String signerPhone,
    String signerEmail) {}
