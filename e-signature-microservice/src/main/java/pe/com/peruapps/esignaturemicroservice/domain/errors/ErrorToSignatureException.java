package pe.com.peruapps.esignaturemicroservice.domain.errors;

public class ErrorToSignatureException extends RuntimeException {
  public ErrorToSignatureException(String message) {
    super(message);
  }
}
