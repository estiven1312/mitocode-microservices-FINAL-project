package pe.com.peruapps.esignaturemicroservice.domain.model;

import java.util.UUID;

public record Id(UUID value) {
    public static Id generate() {
        return new Id(UUID.randomUUID());
    }
}
