package pe.com.peruapps.authservice.controller.dto;

public record TokenValidationResponse(
        boolean valid,
        String username,
        long expiresAtEpochSeconds
) {
}

