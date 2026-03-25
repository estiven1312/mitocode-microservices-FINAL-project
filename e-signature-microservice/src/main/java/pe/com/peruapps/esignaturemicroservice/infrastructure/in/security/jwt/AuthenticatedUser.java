package pe.com.peruapps.esignaturemicroservice.infrastructure.in.security.jwt;

import java.util.Set;

public record AuthenticatedUser(String subject, Set<String> authorities) {}

