package pe.com.peruapps.contractsmicroservice.infrastructure.client.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreatePaymentResponse(
    UUID id,
    String codePayment,
    String description,
    Currency currency,
    BigDecimal amount,
    LocalDateTime timestamp,
    Status status
) {

  public record Currency(UUID id, String isoCode, String iso3Code, Boolean active) {}

  public enum Status {
    PENDING,
    APPROVED,
    REJECTED,
    NOT_PROCESSED
  }
}
