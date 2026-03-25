package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractResponse;
import pe.com.peruapps.contractsmicroservice.application.dto.CreateContractRequest;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractMapper;
import pe.com.peruapps.contractsmicroservice.application.use_case.CreateContractUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@Service
@RequiredArgsConstructor
public class CreateContractUseCaseImpl implements CreateContractUseCase {

  private final ContractRepository contractRepository;
  private final ContractMapper contractMapper;

  @Override
  @Transactional
  public ContractResponse execute(CreateContractRequest request) {
    if (request.type() == Contract.Type.ADDENDUM) {
      if (request.relatedContractId() == null) {
        throw new IllegalArgumentException(
            "An addendum must reference a related contract (relatedContractId is required).");
      }

      Contract.ContractId relatedId = request.relatedContractId();
      Contract relatedContract = contractRepository.findById(relatedId)
          .orElseThrow(() -> new ContractNotFound(relatedId,
              "Related contract not found with id: " + relatedId.value()));

      if (relatedContract.getStatus() != Contract.Status.APPROVED_BY_LEGAL) {
        throw new IllegalStateException(
            "The related contract must be in APPROVED_BY_LEGAL status to create an addendum, "
                + "but current status is: " + relatedContract.getStatus());
      }
    }

    Contract contract = contractMapper.toDomain(request);
    Contract saved = contractRepository.save(contract);
    return contractMapper.toResponse(saved);
  }
}
