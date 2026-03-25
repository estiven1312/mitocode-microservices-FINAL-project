package pe.com.peruapps.authservice.service;

import pe.com.peruapps.authservice.controller.dto.AuthRequest;
import pe.com.peruapps.authservice.controller.dto.AuthResponse;
import pe.com.peruapps.authservice.controller.dto.RefreshTokenRequest;
import pe.com.peruapps.authservice.controller.dto.TokenValidationRequest;
import pe.com.peruapps.authservice.controller.dto.TokenValidationResponse;

public interface AuthService {

    AuthResponse authenticate(AuthRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    TokenValidationResponse validateToken(TokenValidationRequest request);
}

