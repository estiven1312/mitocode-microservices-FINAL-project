package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.AddObligationRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.ObligationResponse;
import pe.com.peruapps.contractsmicroservice.application.port.ContractPerformanceRepository;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractPerformanceMapper;
import pe.com.peruapps.contractsmicroservice.application.use_case.AddObligationUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.Obligation;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@Service
@RequiredArgsConstructor
public class AddObligationUseCaseImpl implements AddObligationUseCase {

  private final ContractRepository contractRepository;
  private final ContractPerformanceRepository performanceRepository;
  private final ContractPerformanceMapper mapper;

  @Override
  @Transactional
  public ObligationResponse execute(Contract.ContractId contractId,
                                    AddObligationRequest request) {
    contractRepository.findById(contractId)
        .orElseThrow(() -> new ContractNotFound(contractId,
            "Contract not found with id: " + contractId.value()));

    Obligation obligation = mapper.toObligationDomain(request);
    obligation.setContractId(contractId);

    Obligation savedObligation = performanceRepository.saveObligation(obligation);
    return mapper.toObligationResponse(savedObligation);
  }
}
