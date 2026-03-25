package pe.com.peruapps.contractsmicroservice.application.use_case.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractResponse;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractStatusTransitionRequest;
import pe.com.peruapps.contractsmicroservice.application.mapper.ContractMapper;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.application.use_case.TransitionContractStatusUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractApproval;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransitionContractStatusUseCaseImpl implements TransitionContractStatusUseCase {

  private final ContractRepository contractRepository;
  private final ContractMapper contractMapper;

  @Override
  @Transactional
  public ContractResponse execute(
      Contract.ContractId contractId, ContractStatusTransitionRequest request) {
    Contract contract =
        contractRepository
            .findById(contractId)
            .orElseThrow(
                () ->
                    new ContractNotFound(
                        contractId, "Contract not found with id: " + contractId.value()));

    switch (request.action()) {
      case SUBMIT_FOR_REVIEW -> contract.submitForReview();

      case SEND_FOR_MANAGER_APPROVAL -> contract.sendForManagerApproval();

      case APPROVE_BY_MANAGER -> {
        ContractApproval approval = buildApprovedRecord(request);
        contract.approveByManager();
        ContractApproval savedApproval = contractRepository.saveApproval(contractId, approval);
        contract.getApprovals().add(savedApproval);
      }

      case SEND_TO_EXTERNAL -> contract.sendToExternal();

      case VALIDATE_BY_EXTERNAL -> {
        ContractApproval approval = buildApprovedRecord(request);
        contract.validateByExternal(approval);
        ContractApproval savedApproval = contractRepository.saveApproval(contractId, approval);
        contract.getApprovals().add(savedApproval);
      }

      case SEND_TO_LEGAL -> contract.sendToLegal();

      case VALIDATE_BY_LEGAL -> {
        ContractApproval approval = buildApprovedRecord(request);
        contract.validateByLegal(approval);
        ContractApproval savedApproval = contractRepository.saveApproval(contractId, approval);
        contract.getApprovals().add(savedApproval);
      }

      case ACTIVATE -> contract.activate();

      case FINISH -> contract.finish();
    }
    contractRepository.save(contract);
    return contractMapper.toResponse(contract);
  }

  private ContractApproval buildApprovedRecord(ContractStatusTransitionRequest request) {
    ContractApproval approval =
        contractMapper.toApproval(
            request.approverId(),
            request.approverEmail(),
            request.approverName(),
            request.comment());
    approval.setStatus(ContractApproval.Status.APPROVED);
    approval.setApprovalDate(LocalDateTime.now());
    return approval;
  }
}
