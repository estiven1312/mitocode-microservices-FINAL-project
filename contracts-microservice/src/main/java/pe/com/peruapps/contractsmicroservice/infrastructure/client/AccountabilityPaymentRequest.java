package pe.com.peruapps.contractsmicroservice.infrastructure.client;

/**
 * HTTP input sent TO the Accountability microservice.
 */
public record AccountabilityPaymentRequest(
    String codePayment
) {}
