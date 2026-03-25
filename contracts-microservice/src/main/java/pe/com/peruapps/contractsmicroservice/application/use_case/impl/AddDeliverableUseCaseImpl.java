package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.AddDeliverableRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.DeliverableResponse;
import pe.com.peruapps.contractsmicroservice.application.port.ContractPerformanceRepository;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractPerformanceMapper;
import pe.com.peruapps.contractsmicroservice.application.use_case.AddDeliverableUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.Deliverable;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@Service
@RequiredArgsConstructor
public class AddDeliverableUseCaseImpl implements AddDeliverableUseCase {

  private final ContractRepository contractRepository;
  private final ContractPerformanceRepository performanceRepository;
  private final ContractPerformanceMapper mapper;

  @Override
  @Transactional
  public DeliverableResponse execute(Contract.ContractId contractId,
                                     AddDeliverableRequest request) {
    contractRepository.findById(contractId)
        .orElseThrow(() -> new ContractNotFound(contractId,
            "Contract not found with id: " + contractId.value()));

    Deliverable deliverable = mapper.toDeliverableDomain(request);
    deliverable.setContractId(contractId);

    Deliverable savedDeliverable = performanceRepository.saveDeliverable(deliverable);
    return mapper.toDeliverableResponse(savedDeliverable);
  }
}
