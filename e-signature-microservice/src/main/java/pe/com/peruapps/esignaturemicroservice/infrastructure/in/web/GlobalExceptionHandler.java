package pe.com.peruapps.esignaturemicroservice.infrastructure.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.com.peruapps.esignaturemicroservice.domain.errors.ErrorToSignatureException;
import pe.com.peruapps.esignaturemicroservice.domain.errors.SignatureNotFoundException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleIllegalArgument(IllegalArgumentException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    problemDetail.setTitle("Invalid request");
    problemDetail.setProperty("timestamp", LocalDateTime.now().toString());
    return problemDetail;
  }

  @ExceptionHandler(SignatureNotFoundException.class)
  public ProblemDetail handleSignatureNotFound(SignatureNotFoundException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    problemDetail.setTitle("Signature not found");
    problemDetail.setProperty("id", exception.getId());
    problemDetail.setProperty("timestamp", LocalDateTime.now().toString());
    return problemDetail;
  }

  @ExceptionHandler(ErrorToSignatureException.class)
  public ProblemDetail handleSignatureError(ErrorToSignatureException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    problemDetail.setTitle("Signature processing error");
    problemDetail.setProperty("timestamp", LocalDateTime.now().toString());
    return problemDetail;
  }

  @ExceptionHandler({InvalidBearerTokenException.class, JwtException.class})
  public ProblemDetail handleInvalidJwtToken(Exception exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Invalid or expired JWT token");
    problemDetail.setTitle("Unauthorized");
    problemDetail.setProperty("timestamp", LocalDateTime.now().toString());
    return problemDetail;
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleAccessDenied(AccessDeniedException exception) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
    problemDetail.setTitle("Forbidden");
    problemDetail.setProperty("timestamp", LocalDateTime.now().toString());
    return problemDetail;
  }
}
