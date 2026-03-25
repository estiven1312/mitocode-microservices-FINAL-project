package pe.com.peruapps.signatureproviderservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ESignatureResponse(
    String codeSignature,
    UUID contractId,
    ESignatureStatus status,
    String urlDocumentSigned,
    String hashSignature,
    String details,
    LocalDateTime timestamp) {}

