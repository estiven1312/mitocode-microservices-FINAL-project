package pe.com.peruapps.signatureproviderservice.dto;

public class ESignatureException extends RuntimeException {
  private final String contractCode;

  public ESignatureException(String contractCode, String message) {
    super(message);
    this.contractCode = contractCode;
  }
}
