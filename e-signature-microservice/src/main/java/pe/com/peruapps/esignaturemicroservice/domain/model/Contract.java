package pe.com.peruapps.esignaturemicroservice.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Contract(
    Id contractId,
    String name,
    String description,
    Contract.Type type,
    Contract.ServiceType serviceType,
    Id relatedContractId,
    LocalDate startDate,
    LocalDate endDate,
    Id thirdPartyId,
    Id createdBy,
    LocalDate createdAt,
    Id requestedArea,
    Id requestedCompany,
    BigDecimal amount,
    Contract.Status status) {
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

  public boolean isValidToSign() {
    return this.status == Status.APPROVED_BY_LEGAL;
  }
}
