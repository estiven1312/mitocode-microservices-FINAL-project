package pe.com.peruapps.contractsmicroservice.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContractApproval {
  private ApprovalId id;
  private Approver approver;
  private String comment;
  private LocalDateTime approvalDate;
  private Status status = Status.PENDING;

  public static class ApprovalId extends Id<Long> {
    public ApprovalId(Long value) {
      super(value);
    }
  }

  public record Approver(Id.UserId approverId, Email approverEmail, String approverName) {}

  public enum Status {
    APPROVED,
    REJECTED,
    PENDING
  }
}
