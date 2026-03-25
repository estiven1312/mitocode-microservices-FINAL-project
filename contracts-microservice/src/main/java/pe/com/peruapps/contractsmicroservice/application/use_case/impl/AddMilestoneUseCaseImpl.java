package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.AddMilestoneRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.MilestoneResponse;
import pe.com.peruapps.contractsmicroservice.application.port.ContractPerformanceRepository;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractPerformanceMapper;
import pe.com.peruapps.contractsmicroservice.application.use_case.AddMilestoneUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractPerformance;
import pe.com.peruapps.contractsmicroservice.domain.entity.Milestone;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@Service
@RequiredArgsConstructor
public class AddMilestoneUseCaseImpl implements AddMilestoneUseCase {

  private final ContractRepository contractRepository;
  private final ContractPerformanceRepository performanceRepository;
  private final ContractPerformanceMapper mapper;

  @Override
  @Transactional
  public MilestoneResponse execute(Contract.ContractId contractId,
                                   AddMilestoneRequest request) {
    contractRepository.findById(contractId)
        .orElseThrow(() -> new ContractNotFound(contractId,
            "Contract not found with id: " + contractId.value()));

    Milestone milestone = mapper.toMilestoneDomain(request);
    milestone.setContractId(contractId);

    Milestone savedMilestone = performanceRepository.saveMilestone(milestone);
    ContractPerformance performance = performanceRepository.findByContractId(contractId)
        .orElseGet(() -> ContractPerformance.forContract(contractId));

    return mapper.toMilestoneResponse(savedMilestone, performance);
  }
}
