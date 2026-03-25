package pe.com.peruapps.authservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pe.com.peruapps.authservice.config.AuthJwtProperties;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

  private final AuthJwtProperties authJwtProperties;
  private final SecretKey signingKey;

  public JwtService(AuthJwtProperties authJwtProperties) {
    this.authJwtProperties = authJwtProperties;
    byte[] keyBytes = Decoders.BASE64.decode(authJwtProperties.secret());
    this.signingKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateAccessToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(
        "roles",
        userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet()));
    return buildToken(claims, userDetails, authJwtProperties.accessTokenExpirationSeconds());
  }

  public String generateRefreshToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("typ", "refresh");
    return buildToken(claims, userDetails, authJwtProperties.refreshTokenExpirationSeconds());
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Instant extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration).toInstant();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  public boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  public boolean isRefreshToken(String token) {
    String tokenType = extractClaim(token, claims -> claims.get("typ", String.class));
    return "refresh".equals(tokenType);
  }

  public long accessTokenExpirationSeconds() {
    return authJwtProperties.accessTokenExpirationSeconds();
  }

  private String buildToken(
      Map<String, Object> extraClaims, UserDetails userDetails, long expirationSeconds) {
    Instant now = Instant.now();
    return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(expirationSeconds)))
        .signWith(signingKey)
        .compact();
  }

  private <T> T extractClaim(String token, Function<Claims, T> resolver) {
    Claims claims =
        Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    return resolver.apply(claims);
  }
}
