package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.MilestoneResponse;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractPerformanceMapper;
import pe.com.peruapps.contractsmicroservice.application.port.ContractPerformanceRepository;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.use_case.GetMilestoneUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.Milestone;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@Service
@RequiredArgsConstructor
public class GetMilestoneUseCaseImpl implements GetMilestoneUseCase {

  private final ContractRepository contractRepository;
  private final ContractPerformanceRepository performanceRepository;
  private final ContractPerformanceMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public MilestoneResponse execute(Contract.ContractId contractId,
                                   Milestone.MilestoneId milestoneId) {
    contractRepository.findById(contractId)
        .orElseThrow(() -> new ContractNotFound(contractId,
            "Contract not found with id: " + contractId.value()));

    Milestone milestone = performanceRepository
        .findMilestoneByContractIdAndId(contractId, milestoneId)
        .orElseThrow(() -> new IllegalArgumentException(
            "Milestone not found with id: " + milestoneId.value()
            + " for contract: " + contractId.value()));

    return mapper.toMilestoneResponse(milestone);
  }
}
