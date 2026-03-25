package pe.com.peruapps.contractsmicroservice.application.dto;

import jakarta.validation.constraints.NotNull;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractApproval;
import pe.com.peruapps.contractsmicroservice.domain.entity.Id;

public record ContractStatusTransitionRequest(
    @NotNull ContractAction action,
    Id.UserId managerId,
    ContractApproval.ApprovalId approvalId,
    Id.UserId approverId,
    String approverEmail,
    String approverName,
    String comment
) {
  public enum ContractAction {
    SUBMIT_FOR_REVIEW,
    SEND_FOR_MANAGER_APPROVAL,
    APPROVE_BY_MANAGER,
    SEND_TO_EXTERNAL,
    VALIDATE_BY_EXTERNAL,
    SEND_TO_LEGAL,
    VALIDATE_BY_LEGAL,
    ACTIVATE,
    FINISH
  }
}
