package pe.com.peruapps.esignaturemicroservice.domain.errors;

public class SignatureNotFoundException extends RuntimeException {
  private final String id;

  public SignatureNotFoundException(String id, String message) {
    super(message);
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
