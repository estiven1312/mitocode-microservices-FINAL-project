package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.use_case.ApplyContractSignedEventUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@Service
@RequiredArgsConstructor
public class ApplyContractSignedEventUseCaseImpl implements ApplyContractSignedEventUseCase {

  private final ContractRepository contractRepository;

  @Override
  @Transactional
  public void execute(Contract.ContractId contractId, String urlSignedFile, String hashSigned) {
    Contract contract = contractRepository.findById(contractId)
        .orElseThrow(() -> new ContractNotFound(contractId,
            "Contract not found with id: " + contractId.value()));

    contract.applySignedData(urlSignedFile, hashSigned);
    contractRepository.save(contract);
  }
}

