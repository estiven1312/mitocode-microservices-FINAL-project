package pe.com.peruapps.contractsmicroservice.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidBearerTokenException.class)
  public ProblemDetail handleInvalidBearerToken(InvalidBearerTokenException ex) {
    String detailMessage = ex.getMessage() != null && !ex.getMessage().isBlank()
        ? ex.getMessage()
        : "Invalid or expired JWT token";
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, detailMessage);
    detail.setTitle("Unauthorized");
    return detail;
  }

  @ExceptionHandler(AuthenticationException.class)
  public ProblemDetail handleAuthentication(AuthenticationException ex) {
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    detail.setTitle("Unauthorized");
    return detail;
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
    detail.setTitle("Forbidden");
    return detail;
  }

  @ExceptionHandler(ContractNotFound.class)
  public ProblemDetail handleContractNotFound(ContractNotFound ex) {
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    detail.setTitle("Contract Not Found");
    return detail;
  }

  @ExceptionHandler(IllegalStateException.class)
  public ProblemDetail handleIllegalState(IllegalStateException ex) {
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(
        HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
    detail.setTitle("Invalid Contract State Transition");
    return detail;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, ex.getMessage());
    detail.setTitle("Bad Request");
    return detail;
  }
}
