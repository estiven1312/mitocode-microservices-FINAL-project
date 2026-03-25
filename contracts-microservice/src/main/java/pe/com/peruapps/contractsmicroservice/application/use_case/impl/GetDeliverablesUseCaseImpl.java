package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.DeliverableResponse;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractPerformanceMapper;
import pe.com.peruapps.contractsmicroservice.application.port.ContractPerformanceRepository;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.use_case.GetDeliverablesUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetDeliverablesUseCaseImpl implements GetDeliverablesUseCase {

  private final ContractRepository contractRepository;
  private final ContractPerformanceRepository performanceRepository;
  private final ContractPerformanceMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public List<DeliverableResponse> execute(Contract.ContractId contractId) {
    contractRepository.findById(contractId)
        .orElseThrow(() -> new ContractNotFound(contractId,
            "Contract not found with id: " + contractId.value()));

    return performanceRepository.findDeliverablesByContractId(contractId).stream()
        .map(mapper::toDeliverableResponse)
        .toList();
  }
}
