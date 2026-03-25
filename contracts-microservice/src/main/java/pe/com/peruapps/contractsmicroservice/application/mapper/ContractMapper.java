package pe.com.peruapps.contractsmicroservice.application.mapper;

import org.springframework.stereotype.Component;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractResponse;
import pe.com.peruapps.contractsmicroservice.application.dto.CreateContractRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.UpdateContractBasicInfoRequest;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractApproval;
import pe.com.peruapps.contractsmicroservice.domain.entity.Email;
import pe.com.peruapps.contractsmicroservice.domain.entity.Id;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class ContractMapper {

  public Contract toDomain(CreateContractRequest request) {
    Contract contract = new Contract();
    contract.setContractId(new Contract.ContractId(UUID.randomUUID()));

    Contract.BasicInformation basicInfo =
        new Contract.BasicInformation(
            request.name(),
            request.description(),
            request.type(),
            request.serviceType(),
            request.type() == Contract.Type.ADDENDUM ? request.relatedContractId() : null,
            request.startDate(),
            request.endDate(),
            request.thirdPartyId(),
            request.createdBy(),
            LocalDate.now(),
            request.requestedArea(),
            request.requestedCompany(),
            request.amount());

    contract.setBasicInformation(basicInfo);
    contract.setUrlFile(request.urlFile());
    return contract;
  }

  public ContractResponse toResponse(Contract contract) {
    var info = contract.getBasicInformation();

    List<ContractResponse.ApprovalResponse> approvalResponses =
        contract.getApprovals().stream()
            .map(
                a ->
                    new ContractResponse.ApprovalResponse(
                        a.getId(),
                        a.getApprover() != null ? a.getApprover().approverId() : null,
                        a.getApprover() != null && a.getApprover().approverEmail() != null
                            ? a.getApprover().approverEmail().value()
                            : null,
                        a.getApprover() != null ? a.getApprover().approverName() : null,
                        a.getComment(),
                        a.getStatus()))
            .toList();

    return new ContractResponse(
        contract.getContractId(),
        info.name(),
        info.description(),
        info.type(),
        info.serviceType(),
        info.relatedContractId(),
        info.startDate(),
        info.endDate(),
        info.thirdPartyId(),
        info.createdBy(),
        info.createdAt(),
        info.requestedArea(),
        info.requestedCompany(),
        info.amount(),
        contract.getUrlFile(),
        contract.getHashSigned(),
        contract.getUrlSignedFile(),
        contract.getStatus(),
        approvalResponses);
  }

  public ContractApproval toApproval(
      Id.UserId approverId, String approverEmail, String approverName, String comment) {
    ContractApproval approval = new ContractApproval();
    Email email = approverEmail != null ? new Email(approverEmail) : null;
    approval.setApprover(new ContractApproval.Approver(approverId, email, approverName));
    approval.setComment(comment);

    return approval;
  }

  public Contract.BasicInformation mergeBasicInformation(
      Contract.BasicInformation current, UpdateContractBasicInfoRequest request) {
    return new Contract.BasicInformation(
        request.name(),
        request.description(),
        request.type(),
        request.serviceType(),
        request.relatedContractId(),
        request.startDate(),
        request.endDate(),
        request.thirdPartyId(),
        current.createdBy(),
        current.createdAt(),
        request.requestedArea(),
        request.requestedCompany(),
        request.amount());
  }
}
