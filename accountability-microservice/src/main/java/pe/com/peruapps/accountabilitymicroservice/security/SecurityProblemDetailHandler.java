package pe.com.peruapps.accountabilitymicroservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pe.com.peruapps.accountabilitymicroservice.controller.WebErrorException;

import java.io.IOException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SecurityProblemDetailHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    ProblemDetail problemDetail = WebErrorException.unauthorizedProblem(resolveUnauthorizedMessage(authException));
    writeProblem(response, problemDetail);
  }

  @Override
  public void handle(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    ProblemDetail problemDetail = WebErrorException.forbiddenProblem(accessDeniedException.getMessage());
    writeProblem(response, problemDetail);
  }

  private void writeProblem(HttpServletResponse response, ProblemDetail problemDetail) throws IOException {
    response.setStatus(problemDetail.getStatus());
    response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
    objectMapper.writeValue(response.getOutputStream(), problemDetail);
  }

  private String resolveUnauthorizedMessage(AuthenticationException ex) {
    if (ex.getCause() instanceof JwtValidationException jwtValidationException) {
      boolean expired = jwtValidationException.getErrors().stream()
          .map(error -> String.valueOf(error.getDescription()))
          .map(description -> description.toLowerCase(Locale.ROOT))
          .anyMatch(description -> description.contains("expired"));
      return expired ? "JWT expirado" : "JWT invalido";
    }

    if (ex.getMessage() == null || ex.getMessage().isBlank()) {
      return "JWT invalido";
    }

    return ex.getMessage();
  }
}

