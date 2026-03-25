package pe.com.peruapps.authservice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record RegisterUserRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotEmpty Set<String> roles
) {
}
