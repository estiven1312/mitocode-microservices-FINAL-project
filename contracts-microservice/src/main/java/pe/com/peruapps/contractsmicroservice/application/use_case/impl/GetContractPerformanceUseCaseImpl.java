package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.*;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractPerformanceMapper;
import pe.com.peruapps.contractsmicroservice.application.port.ContractPerformanceRepository;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.use_case.GetContractPerformanceUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractPerformance;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetContractPerformanceUseCaseImpl implements GetContractPerformanceUseCase {

  private final ContractRepository contractRepository;
  private final ContractPerformanceRepository performanceRepository;
  private final ContractPerformanceMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public PerformanceResponse execute(Contract.ContractId contractId) {
    contractRepository.findById(contractId)
        .orElseThrow(() -> new ContractNotFound(contractId,
            "Contract not found with id: " + contractId.value()));

    ContractPerformance performance = performanceRepository.findByContractId(contractId)
        .orElseGet(() -> ContractPerformance.forContract(contractId));

    List<DeliverableResponse> deliverables = performance.getDeliverables().stream()
        .map(mapper::toDeliverableResponse)
        .toList();

    List<MilestoneResponse> milestones = performance.getMilestones().stream()
        .map(m -> mapper.toMilestoneResponse(m, performance))
        .toList();

    List<ObligationResponse> obligations = performance.getObligations().stream()
        .map(mapper::toObligationResponse)
        .toList();

    List<PaymentResponse> payments = performance.getPayments().stream()
        .map(mapper::toPaymentResponse)
        .toList();

    return new PerformanceResponse(deliverables, milestones, obligations, payments);
  }
}
