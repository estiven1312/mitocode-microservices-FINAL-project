package pe.com.peruapps.authservice.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenValidationRequest(
        @NotBlank String token
) {
}

