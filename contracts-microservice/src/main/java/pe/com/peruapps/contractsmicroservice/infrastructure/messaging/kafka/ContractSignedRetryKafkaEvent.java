package pe.com.peruapps.contractsmicroservice.infrastructure.messaging.kafka;

import java.util.UUID;

public record ContractSignedRetryKafkaEvent(UUID contractId) {}

