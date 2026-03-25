package pe.com.peruapps.accountabilitymicroservice.service.dto;

public record PaymentValidationResponse(String codePayment, boolean valid, String message) {}
