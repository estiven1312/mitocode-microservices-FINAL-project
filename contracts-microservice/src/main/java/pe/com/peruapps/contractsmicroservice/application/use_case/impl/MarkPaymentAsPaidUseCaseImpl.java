package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.MarkPaymentAsPaidRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.PaymentResponse;
import pe.com.peruapps.contractsmicroservice.application.port.AccountabilityPort;
import pe.com.peruapps.contractsmicroservice.application.port.ContractPerformanceRepository;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractPerformanceMapper;
import pe.com.peruapps.contractsmicroservice.application.use_case.MarkPaymentAsPaidUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.Payment;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@Service
@RequiredArgsConstructor
public class MarkPaymentAsPaidUseCaseImpl implements MarkPaymentAsPaidUseCase {

  private final ContractRepository contractRepository;
  private final ContractPerformanceRepository performanceRepository;
  private final AccountabilityPort accountabilityPort;
  private final ContractPerformanceMapper mapper;

  @Override
  public PaymentResponse execute(Contract.ContractId contractId,
                                 MarkPaymentAsPaidRequest request) {
    contractRepository.findById(contractId)
        .orElseThrow(() -> new ContractNotFound(contractId,
            "Contract not found with id: " + contractId.value()));

    Payment payment = performanceRepository
        .findPaymentByContractIdAndPaymentId(contractId, request.paymentId())
        .orElseThrow(() -> new IllegalArgumentException(
            "Payment not found with id: " + request.paymentId().value()
            + " for contract: " + contractId.value()));

    AccountabilityPort.AccountabilityPaymentResult result =
        accountabilityPort.validatePayment(payment.getCodePayment());

    if (!result.valid()) {
      throw new IllegalStateException(
          "Accountability validation failed for payment code '"
              + payment.getCodePayment() + "': " + result.message());
    }

    // Mark as paid in domain
    payment.markAsPaid();

    Payment saved = performanceRepository.savePayment(payment);

    return mapper.toPaymentResponse(saved);
  }
}
