package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractResponse;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractMapper;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.use_case.GetContractUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@Service
@RequiredArgsConstructor
public class GetContractUseCaseImpl implements GetContractUseCase {

  private final ContractRepository contractRepository;
  private final ContractMapper contractMapper;

  @Override
  @Transactional(readOnly = true)
  public ContractResponse execute(Contract.ContractId contractId) {
    Contract contract = contractRepository.findById(contractId)
        .orElseThrow(() -> new ContractNotFound(contractId,
            "Contract not found with id: " + contractId.value()));
    return contractMapper.toResponse(contract);
  }
}
