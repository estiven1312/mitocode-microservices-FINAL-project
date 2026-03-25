package pe.com.peruapps.accountabilitymicroservice.service.dto;

import pe.com.peruapps.accountabilitymicroservice.model.Currency;

import java.math.BigDecimal;

public record PaymentRequest(
    String codePayment,
    String description,
    Currency.CurrencyCode currency,
    BigDecimal amount
) {}
