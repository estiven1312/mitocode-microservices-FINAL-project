package pe.com.peruapps.contractsmicroservice.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Deliverable {
  public record DeliverableId(Long value) {}

  private DeliverableId id;
  private String name;
  private String description;
  private Contract.ContractId contractId;
  private LocalDate scheduleDate;
  private LocalDateTime deliveredAt;
  private LocalDateTime acceptedAt;
  private Status status = Status.PENDING;

  public void markAsInProgress() {
    if (this.status != Status.PENDING && this.status != Status.REJECTED) {
      throw new IllegalStateException(
          "Only pending or rejected deliverables can be marked as in progress.");
    }
    this.status = Status.IN_PROGRESS;
  }

  public void markAsDelivered() {
    if (this.status != Status.IN_PROGRESS) {
      throw new IllegalStateException("Only in-progress deliverables can be marked as delivered.");
    }
    this.deliveredAt = LocalDateTime.now();
    this.status = Status.DELIVERED;
  }

  public void markAsAccepted() {
    if (this.status != Status.DELIVERED) {
      throw new IllegalStateException("Only delivered deliverables can be marked as accepted.");
    }
    this.acceptedAt = LocalDateTime.now();
    this.status = Status.ACCEPTED;
  }

  public void markAsRejected() {
    if (this.status != Status.DELIVERED) {
      throw new IllegalStateException("Only delivered deliverables can be marked as rejected.");
    }
    this.status = Status.REJECTED;
  }

  public void markAsOverdue() {
    if (this.status != Status.IN_PROGRESS) {
      throw new IllegalStateException("Only in-progress deliverables can be marked as overdue.");
    }
    this.status = Status.OVERDUE;
  }

  public enum Status {
    PENDING,
    IN_PROGRESS,
    DELIVERED,
    ACCEPTED,
    REJECTED,
    OVERDUE
  }
}
