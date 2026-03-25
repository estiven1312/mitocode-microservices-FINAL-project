package pe.com.peruapps.esignaturemicroservice.infrastructure.out.messaging.kafka;

import java.util.UUID;

public record ContractSignedRetryKafkaEvent(UUID contractId) {}

