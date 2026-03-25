package pe.com.peruapps.contractsmicroservice.infrastructure.messaging.kafka;

import java.util.UUID;

public record ContractSignedKafkaEvent(UUID contractId, String urlSignedFile, String hashSigned, Operation op) {
    enum Operation {
        SIGN,
        DELETE
    }
}

