package pe.com.peruapps.contractsmicroservice.infrastructure.client.dto;

import pe.com.peruapps.contractsmicroservice.domain.entity.Payment;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        String codePayment,
        String description,
        Payment.Currency currency,
        BigDecimal amount
) {

}
