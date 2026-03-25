package pe.com.peruapps.esignaturemicroservice.infrastructure.in.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import pe.com.peruapps.esignaturemicroservice.infrastructure.in.security.config.JwtProperties;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class JwtTokenService {

  private final SecretKey signingKey;

  public JwtTokenService(JwtProperties jwtProperties) {
    if (jwtProperties.getSecret() == null || jwtProperties.getSecret().isBlank()) {
      throw new IllegalStateException("auth.jwt.secret must be configured");
    }
    this.signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.getSecret()));
  }

  public Optional<AuthenticatedUser> validateAndExtract(String token) {
    try {
      Claims claims =
          Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();

      String subject = claims.getSubject();
      if (subject == null || subject.isBlank()) {
        return Optional.empty();
      }

      return Optional.of(new AuthenticatedUser(subject, extractAuthorities(claims)));
    } catch (RuntimeException ex) {
      return Optional.empty();
    }
  }

  private Set<String> extractAuthorities(Claims claims) {
    Set<String> authorities = new HashSet<>();

    // Roles from auth-service token are treated as Spring authorities.
    addCollectionClaim(authorities, claims.get("roles", Collection.class));


    return authorities;
  }

  private void addCollectionClaim(Set<String> target, Collection<?> values) {
    if (values == null) {
      return;
    }
    for (Object value : values) {
      if (value != null) {
        target.add(value.toString());
      }
    }
  }
}
