package pe.com.peruapps.accountabilitymicroservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.com.peruapps.accountabilitymicroservice.model.DuplicatePaymentException;

@RestControllerAdvice
public class WebErrorException {

  @ExceptionHandler(DuplicatePaymentException.class)
  public ProblemDetail handleDuplicatePayment(DuplicatePaymentException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    problemDetail.setTitle("Duplicate Payment");
    problemDetail.setDetail("A payment with the same code already exists. " + ex.getMessage());
    return problemDetail;
  }

  @ExceptionHandler(AuthenticationException.class)
  public ProblemDetail handleAuthentication(AuthenticationException ex) {
    return unauthorizedProblem(ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
    return forbiddenProblem(ex.getMessage());
  }

  public static ProblemDetail unauthorizedProblem(String detail) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
    problemDetail.setTitle("Unauthorized");
    problemDetail.setDetail((detail == null || detail.isBlank()) ? "JWT invalido" : detail);
    return problemDetail;
  }

  public static ProblemDetail forbiddenProblem(String detail) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
    problemDetail.setTitle("Forbidden");
    problemDetail.setDetail((detail == null || detail.isBlank()) ? "Acceso denegado" : detail);
    return problemDetail;
  }
}
