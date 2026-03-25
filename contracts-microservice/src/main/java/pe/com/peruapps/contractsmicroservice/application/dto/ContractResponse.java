package pe.com.peruapps.contractsmicroservice.application.dto;

import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractApproval;
import pe.com.peruapps.contractsmicroservice.domain.entity.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ContractResponse(
    Contract.ContractId contractId,
    String name,
    String description,
    Contract.Type type,
    Contract.ServiceType serviceType,
    Contract.ContractId relatedContractId,
    LocalDate startDate,
    LocalDate endDate,
    Id.ThirdPartyId thirdPartyId,
    Id.UserId createdBy,
    LocalDate createdAt,
    Id.OrganizationalEntityId requestedArea,
    Id.OrganizationalEntityId requestedCompany,
    BigDecimal amount,
    String urlFile,
    String hashSignature,
    String urlSignedFile,
    Contract.Status status,
    List<ApprovalResponse> approvals
) {
  public record ApprovalResponse(
      ContractApproval.ApprovalId approvalId,
      Id.UserId approverId,
      String approverEmail,
      String approverName,
      String comment,
      ContractApproval.Status status
  ) {}
}
