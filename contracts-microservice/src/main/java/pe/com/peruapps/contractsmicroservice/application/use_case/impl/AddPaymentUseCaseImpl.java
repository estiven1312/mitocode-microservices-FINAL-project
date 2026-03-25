package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.AddPaymentRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.PaymentResponse;
import pe.com.peruapps.contractsmicroservice.application.port.AccountabilityPort;
import pe.com.peruapps.contractsmicroservice.application.port.ContractPerformanceRepository;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractPerformanceMapper;
import pe.com.peruapps.contractsmicroservice.application.use_case.AddPaymentUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.Payment;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@Service
@RequiredArgsConstructor
public class AddPaymentUseCaseImpl implements AddPaymentUseCase {

  private final ContractRepository contractRepository;
  private final ContractPerformanceRepository performanceRepository;
  private final ContractPerformanceMapper mapper;
  private final AccountabilityPort accountabilityPort;

  @Override
  @Transactional
  public PaymentResponse execute(Contract.ContractId contractId, AddPaymentRequest request) {
    contractRepository
        .findById(contractId)
        .orElseThrow(
            () ->
                new ContractNotFound(
                    contractId, "Contract not found with id: " + contractId.value()));
    var response = accountabilityPort.createPayment(
            contractId,
            request);

    Payment payment = mapper.toPaymentDomain(request);
    payment.setContractId(contractId);
    payment.setCodePayment(response.codePayment());
    Payment savedPayment = performanceRepository.createPayment(payment);


    return mapper.toPaymentResponse(savedPayment);
  }
}
