package pe.com.peruapps.signatureproviderservice.dto;

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
