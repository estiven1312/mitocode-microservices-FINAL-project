package pe.com.peruapps.esignaturemicroservice.infrastructure.in.web;

import java.time.LocalDateTime;
import java.util.UUID;

public record SignatureResponse(
    UUID id,
    UUID contractId,
    String urlDocumentSigned,
    String hashDocument,
    String status,
    LocalDateTime signedAt,
    String details) {}

