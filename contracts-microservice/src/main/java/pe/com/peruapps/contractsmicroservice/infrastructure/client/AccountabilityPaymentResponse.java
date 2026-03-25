package pe.com.peruapps.contractsmicroservice.infrastructure.client;

/**
 * HTTP response received FROM the Accountability microservice.
 */
public record AccountabilityPaymentResponse(
    String codePayment,
    boolean valid,
    String message
) {}
