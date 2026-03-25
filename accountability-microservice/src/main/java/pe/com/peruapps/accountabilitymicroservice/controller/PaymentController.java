package pe.com.peruapps.accountabilitymicroservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.com.peruapps.accountabilitymicroservice.controller.dto.ChangePaymentStatusRequest;
import pe.com.peruapps.accountabilitymicroservice.model.Payment;
import pe.com.peruapps.accountabilitymicroservice.service.PaymentService;
import pe.com.peruapps.accountabilitymicroservice.service.dto.PaymentRequest;
import pe.com.peruapps.accountabilitymicroservice.service.dto.PaymentValidationResponse;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;

  @GetMapping
  public List<Payment> listByStatus(
      @RequestParam(name = "status", required = false, defaultValue = "NOT_PROCESSED")
          Payment.Status status) {
    return paymentService.getPaymentsByStatus(status);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Payment createPayment(@RequestBody PaymentRequest request) {
    return paymentService.savePayment(request);
  }

  @GetMapping("/{codePayment}")
  public Payment getByCode(@PathVariable String codePayment) {
    return paymentService.getPaymentByCode(codePayment);
  }

  @GetMapping("/{codePayment}/validate")
  public PaymentValidationResponse validate(@PathVariable String codePayment) {
    return paymentService.validatePayment(codePayment);
  }

  @PatchMapping("/{codePayment}/status")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateStatus(
      @PathVariable String codePayment, @RequestBody ChangePaymentStatusRequest request) {
    paymentService.updatePaymentStatus(codePayment, request.status());
  }

}
