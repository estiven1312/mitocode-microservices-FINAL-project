package pe.com.peruapps.authservice.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pe.com.peruapps.authservice.controller.dto.AuthRequest;
import pe.com.peruapps.authservice.controller.dto.AuthResponse;
import pe.com.peruapps.authservice.controller.dto.RefreshTokenRequest;
import pe.com.peruapps.authservice.controller.dto.TokenValidationRequest;
import pe.com.peruapps.authservice.controller.dto.TokenValidationResponse;
import pe.com.peruapps.authservice.domain.exception.exception.InvalidCredentialsException;
import pe.com.peruapps.authservice.security.JwtService;
import pe.com.peruapps.authservice.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;

  public AuthServiceImpl(
      AuthenticationManager authenticationManager,
      UserDetailsService userDetailsService,
      JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtService = jwtService;
  }

  @Override
  public AuthResponse authenticate(AuthRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    } catch (BadCredentialsException ex) {
      throw new InvalidCredentialsException();
    }

    UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
    return buildAuthResponse(userDetails);
  }

  @Override
  public AuthResponse refreshToken(RefreshTokenRequest request) {
    try {
      if (!jwtService.isRefreshToken(request.refreshToken())) {
        throw new InvalidCredentialsException();
      }

      String username = jwtService.extractUsername(request.refreshToken());
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      if (!jwtService.isTokenValid(request.refreshToken(), userDetails)) {
        throw new InvalidCredentialsException();
      }

      return buildAuthResponse(userDetails);
    } catch (RuntimeException ex) {
      throw new InvalidCredentialsException();
    }
  }

  @Override
  public TokenValidationResponse validateToken(TokenValidationRequest request) {
    try {
      String username = jwtService.extractUsername(request.token());
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      boolean valid = jwtService.isTokenValid(request.token(), userDetails);
      long expiration = jwtService.extractExpiration(request.token()).getEpochSecond();
      return new TokenValidationResponse(valid, username, expiration);
    } catch (RuntimeException ex) {
      return new TokenValidationResponse(false, null, 0L);
    }
  }

  private AuthResponse buildAuthResponse(UserDetails userDetails) {
    String accessToken = jwtService.generateAccessToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);
    return new AuthResponse(
        accessToken, refreshToken, "Bearer", jwtService.accessTokenExpirationSeconds());
  }
}
