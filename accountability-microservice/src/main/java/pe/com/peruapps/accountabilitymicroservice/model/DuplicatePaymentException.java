package pe.com.peruapps.accountabilitymicroservice.model;

public class DuplicatePaymentException extends RuntimeException {

  public DuplicatePaymentException(String codePayment) {
    super("A payment with code '" + codePayment + "' already exists.");
  }
}
