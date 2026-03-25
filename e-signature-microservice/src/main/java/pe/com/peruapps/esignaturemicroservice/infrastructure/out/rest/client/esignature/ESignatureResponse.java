package pe.com.peruapps.esignaturemicroservice.infrastructure.out.rest.client.esignature;

import java.time.LocalDateTime;
import java.util.UUID;

public record ESignatureResponse(
    String codeSignature,
    UUID contractId,
    String status,
    String urlDocumentSigned,
    String hashSignature,
    String details,
    LocalDateTime timestamp) {}
