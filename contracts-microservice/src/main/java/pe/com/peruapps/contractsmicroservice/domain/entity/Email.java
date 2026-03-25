package pe.com.peruapps.contractsmicroservice.domain.entity;

public record Email(String value) {
  public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

  public Email {
    if (value != null && !value.matches(EMAIL_REGEX)) {
      throw new IllegalArgumentException("Invalid email format: " + value);
    }
  }
}
