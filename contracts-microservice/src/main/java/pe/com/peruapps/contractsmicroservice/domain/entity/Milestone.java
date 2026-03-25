package pe.com.peruapps.contractsmicroservice.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
public class Milestone {
  private MilestoneId id;
  private String name;
  private String description;
  private LocalDate dueDate;
  private LocalDateTime completedAt;
  private Status status = Status.PENDING;
  private Contract.ContractId contractId;
  private Deliverable.DeliverableId deliverableId;

  public record MilestoneId(Long value) {}
  public enum Status {
    PENDING,
    COMPLETED,
    OVERDUE
  }
}
