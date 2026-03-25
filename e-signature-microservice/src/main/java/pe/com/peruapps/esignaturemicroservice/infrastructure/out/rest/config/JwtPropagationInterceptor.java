package pe.com.peruapps.esignaturemicroservice.infrastructure.out.rest.config;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtPropagationInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public @NonNull ClientHttpResponse intercept(
          HttpRequest request, byte @NonNull [] body, @NonNull ClientHttpRequestExecution execution) throws IOException {

    if (!request.getHeaders().containsHeader(HttpHeaders.AUTHORIZATION)) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication instanceof JwtAuthenticationToken jwtAuth) {
        String tokenValue = jwtAuth.getToken().getTokenValue();
        request.getHeaders().setBearerAuth(tokenValue);
      }
    }

    return execution.execute(request, body);
  }
}
