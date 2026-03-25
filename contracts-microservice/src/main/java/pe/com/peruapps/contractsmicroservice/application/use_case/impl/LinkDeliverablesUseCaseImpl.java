package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.LinkDeliverablesRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.MilestoneResponse;
import pe.com.peruapps.contractsmicroservice.application.port.ContractPerformanceRepository;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractPerformanceMapper;
import pe.com.peruapps.contractsmicroservice.application.use_case.LinkDeliverablesUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractPerformance;
import pe.com.peruapps.contractsmicroservice.domain.entity.Deliverable;
import pe.com.peruapps.contractsmicroservice.domain.entity.Milestone;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@Service
@RequiredArgsConstructor
public class LinkDeliverablesUseCaseImpl implements LinkDeliverablesUseCase {

  private final ContractRepository contractRepository;
  private final ContractPerformanceRepository performanceRepository;
  private final ContractPerformanceMapper mapper;

  @Override
  @Transactional
  public MilestoneResponse execute(Contract.ContractId contractId,
                                   LinkDeliverablesRequest request) {
    contractRepository.findById(contractId)
        .orElseThrow(() -> new ContractNotFound(contractId,
            "Contract not found with id: " + contractId.value()));

    ContractPerformance performance = performanceRepository.findByContractId(contractId)
        .orElseThrow(() -> new IllegalStateException(
            "No performance found for contract: " + contractId.value()
                + ". Add at least one milestone and deliverable first."));

    validateBelongToContract(contractId, request, performance);

    performance.linkDeliveablesToMilestone(request.milestoneId(), request.deliverableIds());

    performanceRepository.saveMilestoneDeliverableLinks(
        contractId, request.milestoneId(), request.deliverableIds());

    ContractPerformance saved = performanceRepository.findByContractId(contractId)
        .orElseThrow(() -> new IllegalStateException(
            "No performance found for contract: " + contractId.value()));

    Milestone milestone = saved.getMilestones().stream()
        .filter(m -> m.getId().equals(request.milestoneId()))
        .findFirst()
        .orElseThrow();

    return mapper.toMilestoneResponse(milestone, saved);
  }

  private void validateBelongToContract(Contract.ContractId contractId,
                                        LinkDeliverablesRequest request,
                                        ContractPerformance performance) {
    boolean milestoneBelongs = performance.getMilestones().stream()
        .anyMatch(m -> m.getId().equals(request.milestoneId()));
    if (!milestoneBelongs) {
      throw new IllegalArgumentException(
          "Milestone " + request.milestoneId().value()
              + " does not belong to contract " + contractId.value());
    }

    Set<Long> contractDeliverableIds = performance.getDeliverables().stream()
        .map(d -> d.getId().value())
        .collect(Collectors.toSet());

    for (Deliverable.DeliverableId deliverableId : request.deliverableIds()) {
      if (!contractDeliverableIds.contains(deliverableId.value())) {
        throw new IllegalArgumentException(
            "Deliverable " + deliverableId.value()
                + " does not belong to contract " + contractId.value());
      }
    }
  }
}
