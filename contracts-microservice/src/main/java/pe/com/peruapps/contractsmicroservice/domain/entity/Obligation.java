package pe.com.peruapps.contractsmicroservice.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Obligation {
  public record ObligationId(Long value) {}

  private ObligationId id;
  public Contract.ContractId contractId;
  private String description;
  private LocalDate dueDate;
  private ObligationStatus status = ObligationStatus.PENDING;

  public void fulfill() {
    this.status = ObligationStatus.FULFILLED;
  }

  public void breach() {
    this.status = ObligationStatus.BREACHED;
  }

  public enum ObligationStatus {
    PENDING,
    FULFILLED,
    BREACHED
  }
}
