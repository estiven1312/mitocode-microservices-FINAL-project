package pe.com.peruapps.contractsmicroservice.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Contract {
  public record ContractId(UUID value) {}

  private ContractId contractId;
  private BasicInformation basicInformation;
  private ContractPerformance performance;
  private String urlFile;
  private String urlSignedFile;
  private String hashSigned;
  private Status status = Status.IN_DRAFT;
  private List<ContractApproval> approvals = new ArrayList<>();

  public void submitForReview() {
    if (this.status != Status.IN_DRAFT) {
      throw new IllegalStateException("Only contracts in draft can be submitted for review.");
    }
    this.status = Status.IN_REVIEW;
  }

  public void sendForManagerApproval() {
    if (this.status != Status.IN_REVIEW) {
      throw new IllegalStateException("Only contracts in review can be sent for manager approval.");
    }

    this.status = Status.PENDING_APPROVAL_BY_MANAGER;
  }

  public void approveByManager() {
    if (this.status != Status.PENDING_APPROVAL_BY_MANAGER) {
      throw new IllegalStateException(
          "Only contracts pending approval by manager can be approved.");
    }

    this.status = Status.APPROVED_BY_MANAGER;
  }

  public void sendToExternal() {
    if (this.status != Status.APPROVED_BY_MANAGER) {
      throw new IllegalStateException(
          "Only contracts approved by manager can be sent to external.");
    }
    this.status = Status.SENT_TO_EXTERNAL;
  }

  public void validateByExternal(ContractApproval approval) {
    if (this.status != Status.SENT_TO_EXTERNAL) {
      throw new IllegalStateException(
          "Only contracts sent to external can be validated by external.");
    }
    this.status = Status.APPROVED_BY_EXTERNAL;
  }

  public void sendToLegal() {
    if (this.status != Status.APPROVED_BY_EXTERNAL) {
      throw new IllegalStateException("Only contracts approved by external can be sent to legal.");
    }
    this.status = Status.SENT_TO_LEGAL;
  }

  public void validateByLegal(ContractApproval approval) {
    if (this.status != Status.SENT_TO_LEGAL) {
      throw new IllegalStateException("Only contracts sent to legal can be validated by legal.");
    }
    this.status = Status.APPROVED_BY_LEGAL;
  }

  public void activate() {
    if (this.status != Status.APPROVED_BY_LEGAL) {
      throw new IllegalStateException("Only contracts approved by legal can be activated.");
    }
    if(basicInformation.startDate != null && basicInformation.startDate.isBefore(LocalDate.now())) {
      throw  new IllegalStateException("Cannot activate a contract with a start date in the past.");
    }
    this.status = Status.ACTIVE;
  }

  public void finish() {
    if (this.status != Status.ACTIVE) {
      throw new IllegalStateException("Only active contracts can be finished.");
    }
    if(basicInformation.endDate != null && basicInformation.endDate.isAfter(LocalDate.now())) {
      throw  new IllegalStateException("Cannot finish a contract with an end date in the future.");
    }
    this.status = Status.FINISHED;
  }

  public void applySignedData(String urlSignedFile, String hashSigned) {
    if (urlSignedFile == null || urlSignedFile.isBlank()) {
      throw new IllegalArgumentException("urlSignedFile is required.");
    }
    if (hashSigned == null || hashSigned.isBlank()) {
      throw new IllegalArgumentException("hashSigned is required.");
    }
    this.urlSignedFile = urlSignedFile;
    this.hashSigned = hashSigned;
  }

  public void clearSignedData() {
    this.urlSignedFile = null;
    this.hashSigned = null;
  }

  public enum Type {
    CONTRACT,
    ADDENDUM
  }

  public enum ServiceType {
    SERVICE,
    GOOD,
    PROPERTY,
    OTHER
  }

  public record BasicInformation(
      String name,
      String description,
      Type type,
      ServiceType serviceType,
      ContractId relatedContractId,
      LocalDate startDate,
      LocalDate endDate,
      Id.ThirdPartyId thirdPartyId,
      Id.UserId createdBy,
      LocalDate createdAt,
      Id.OrganizationalEntityId requestedArea,
      Id.OrganizationalEntityId requestedCompany,
      BigDecimal amount) {}

  public enum Status {
    IN_DRAFT,
    IN_REVIEW,
    PENDING_APPROVAL_BY_MANAGER,
    APPROVED_BY_MANAGER,
    SENT_TO_EXTERNAL,
    PENDING_VALIDATION_BY_EXTERNAL,
    APPROVED_BY_EXTERNAL,
    SENT_TO_LEGAL,
    PENDING_VALIDATION_BY_LEGAL,
    APPROVED_BY_LEGAL,
    ACTIVE,
    FINISHED
  }
}
