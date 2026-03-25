package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractResponse;
import pe.com.peruapps.contractsmicroservice.application.dto.UpdateContractBasicInfoRequest;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractMapper;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.use_case.UpdateContractBasicInfoUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

@Service
@RequiredArgsConstructor
public class UpdateContractBasicInfoUseCaseImpl implements UpdateContractBasicInfoUseCase {

  private final ContractRepository contractRepository;
  private final ContractMapper contractMapper;

  @Override
  @Transactional
  public ContractResponse execute(Contract.ContractId contractId,
                                  UpdateContractBasicInfoRequest request) {
    Contract contract = contractRepository.findById(contractId)
        .orElseThrow(() -> new ContractNotFound(contractId,
            "Contract not found with id: " + contractId.value()));

    validateAddendum(contractId, request);

    Contract.BasicInformation merged = contractMapper.mergeBasicInformation(
        contract.getBasicInformation(), request);
    contract.setBasicInformation(merged);

    Contract saved = contractRepository.save(contract);
    return contractMapper.toResponse(saved);
  }

  private void validateAddendum(Contract.ContractId contractId,
                                UpdateContractBasicInfoRequest request) {
    if (request.type() != Contract.Type.ADDENDUM) {
      return;
    }

    if (request.relatedContractId() == null) {
      throw new IllegalArgumentException(
          "An addendum must reference a related contract (relatedContractId is required).");
    }

    if (contractId.equals(request.relatedContractId())) {
      throw new IllegalArgumentException("A contract cannot be an addendum of itself.");
    }

    Contract relatedContract = contractRepository.findById(request.relatedContractId())
        .orElseThrow(() -> new ContractNotFound(request.relatedContractId(),
            "Related contract not found with id: " + request.relatedContractId().value()));

    if (relatedContract.getStatus() != Contract.Status.APPROVED_BY_LEGAL) {
      throw new IllegalStateException(
          "The related contract must be in APPROVED_BY_LEGAL status to set ADDENDUM type, "
              + "but current status is: " + relatedContract.getStatus());
    }
  }
}

