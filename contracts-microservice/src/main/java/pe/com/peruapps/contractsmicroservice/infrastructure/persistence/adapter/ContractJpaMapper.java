package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.adapter;
import pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity.*;

import org.springframework.stereotype.Component;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractApproval;
import pe.com.peruapps.contractsmicroservice.domain.entity.Email;
import pe.com.peruapps.contractsmicroservice.domain.entity.Id;

import java.util.ArrayList;
import java.util.List;

@Component
class ContractJpaMapper {

  ContractJpaEntity toEntity(Contract contract) {
    var info = contract.getBasicInformation();
    ContractJpaEntity entity = new ContractJpaEntity();
    entity.setContractId(contract.getContractId().value());
    entity.setName(info.name());
    entity.setDescription(info.description());
    entity.setType(info.type());
    entity.setServiceType(info.serviceType());
    entity.setRelatedContractId(
        info.relatedContractId() != null ? info.relatedContractId().value() : null);
    entity.setStartDate(info.startDate());
    entity.setEndDate(info.endDate());
    entity.setThirdPartyId(info.thirdPartyId().getValue());
    entity.setCreatedBy(info.createdBy().getValue());
    entity.setCreatedAt(info.createdAt());
    entity.setRequestedArea(info.requestedArea().getValue());
    entity.setRequestedCompany(info.requestedCompany().getValue());
    entity.setAmount(info.amount());
    entity.setStatus(contract.getStatus());
    entity.setUrlFile(contract.getUrlFile());
    entity.setHashSignature(contract.getHashSigned());
    entity.setUrlSignedFile(contract.getUrlSignedFile());

    return entity;
  }

  Contract toDomain(ContractJpaEntity entity) {
    Contract contract = new Contract();
    contract.setContractId(new Contract.ContractId(entity.getContractId()));

    Contract.ContractId relatedId = entity.getRelatedContractId() != null
        ? new Contract.ContractId(entity.getRelatedContractId())
        : null;

    Contract.BasicInformation info = new Contract.BasicInformation(
        entity.getName(),
        entity.getDescription(),
        entity.getType(),
        entity.getServiceType(),
        relatedId,
        entity.getStartDate(),
        entity.getEndDate(),
        new Id.ThirdPartyId(entity.getThirdPartyId()),
        new Id.UserId(entity.getCreatedBy()),
        entity.getCreatedAt(),
        new Id.OrganizationalEntityId(entity.getRequestedArea()),
        new Id.OrganizationalEntityId(entity.getRequestedCompany()),
        entity.getAmount()
    );

    contract.setBasicInformation(info);
    contract.setUrlFile(entity.getUrlFile());
    contract.setHashSigned(entity.getHashSignature());
    contract.setUrlSignedFile(entity.getUrlSignedFile());
    contract.setStatus(entity.getStatus());

    List<ContractApproval> approvals = new ArrayList<>();
    if (entity.getApprovals() != null) {
      for (ContractApprovalJpaEntity approvalJpaEntity : entity.getApprovals()) {
        approvals.add(toApprovalDomain(approvalJpaEntity));
      }
    }
    contract.setApprovals(approvals);
    return contract;
  }

  ContractApprovalJpaEntity toApprovalEntity(ContractApproval approval,
                                             ContractJpaEntity contract) {
    ContractApprovalJpaEntity entity = new ContractApprovalJpaEntity();
    entity.setContract(contract);
    if (approval.getId() != null) {
      entity.setId(approval.getId().getValue());
    }
    if (approval.getApprover() != null) {
      var approver = approval.getApprover();
      entity.setApproverId(
          approver.approverId() != null ? approver.approverId().getValue() : null);
      entity.setApproverEmail(
          approver.approverEmail() != null ? approver.approverEmail().value() : null);
      entity.setApproverName(approver.approverName());
    }
    entity.setComment(approval.getComment());
    entity.setApprovalDate(approval.getApprovalDate());
    entity.setStatus(approval.getStatus());
    return entity;
  }

  ContractApproval toApprovalDomain(ContractApprovalJpaEntity entity) {
    ContractApproval approval = new ContractApproval();
    approval.setId(new ContractApproval.ApprovalId(entity.getId()));
    Email email = entity.getApproverEmail() != null ? new Email(entity.getApproverEmail()) : null;
    Id.UserId userId = entity.getApproverId() != null ? new Id.UserId(entity.getApproverId()) : null;
    approval.setApprover(new ContractApproval.Approver(userId, email, entity.getApproverName()));
    approval.setComment(entity.getComment());
    approval.setApprovalDate(entity.getApprovalDate());
    approval.setStatus(entity.getStatus());
    return approval;
  }
}
