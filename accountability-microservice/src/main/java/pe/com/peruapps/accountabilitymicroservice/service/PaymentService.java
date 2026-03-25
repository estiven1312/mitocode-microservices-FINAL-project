package pe.com.peruapps.accountabilitymicroservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.peruapps.accountabilitymicroservice.model.Currency;
import pe.com.peruapps.accountabilitymicroservice.model.DuplicatePaymentException;
import pe.com.peruapps.accountabilitymicroservice.model.Payment;
import pe.com.peruapps.accountabilitymicroservice.repository.CurrencyRepository;
import pe.com.peruapps.accountabilitymicroservice.repository.PaymentRepository;
import pe.com.peruapps.accountabilitymicroservice.service.dto.PaymentRequest;
import pe.com.peruapps.accountabilitymicroservice.service.dto.PaymentValidationResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentRepository paymentRepository;
  private final CurrencyRepository currencyRepository;

  public List<Payment> getPaymentsByStatus(Payment.Status status) {
    return paymentRepository.findByStatus(status.name());
  }

  public Payment savePayment(PaymentRequest request) {
    paymentRepository
        .findByCodePayment(request.codePayment())
        .ifPresent(
            p -> {
              throw new DuplicatePaymentException(request.codePayment());
            });

    Currency currency =
        currencyRepository
            .findByIsoCode(request.currency().getIsoCode())
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "Currency with iso code " + request.currency() + " not found"));

    Payment payment = new Payment();
    payment.setCodePayment(request.codePayment());
    payment.setDescription(request.description());
    payment.setAmount(request.amount());
    payment.setCurrency(currency);
    payment.setStatus(Payment.Status.PENDING);
    return paymentRepository.save(payment);
  }

  public Payment getPaymentByCode(String codePayment) {
    return paymentRepository
        .findByCodePayment(codePayment)
        .orElseThrow(() -> new RuntimeException("Payment with code " + codePayment + " not found"));
  }

  public PaymentValidationResponse validatePayment(String codePayment) {
    Payment payment = getPaymentByCode(codePayment);
    boolean valid = payment.getStatus() == Payment.Status.APPROVED;
    String message = valid ? "Payment is valid" : "Payment is not valid";
    return new PaymentValidationResponse(codePayment, valid, message);
  }

  public void updatePaymentStatus(String codePayment, Payment.Status status) {
    paymentRepository
        .findByCodePayment(codePayment)
        .ifPresentOrElse(
            payment -> {
              payment.setStatus(status);
              if (status == Payment.Status.APPROVED || status == Payment.Status.REJECTED) {
                payment.setTimestamp(LocalDateTime.now());
              }
              paymentRepository.save(payment);
            },
            () -> {
              throw new RuntimeException("Payment with code " + codePayment + " not found");
            });
  }
}
