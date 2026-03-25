package pe.com.peruapps.contractsmicroservice.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Payment {

  private PaymentId id;
  private Contract.ContractId contractId;
  private String codePayment;
  private BigDecimal amount;
  private Currency currency;
  private LocalDate dueDate;
  private LocalDate paidAt;
  private Status status = Status.PENDING;

  public void markAsPaid() {
    if (this.status == Status.PAID) {
      throw new IllegalStateException("Payment is already marked as paid.");
    }
    this.paidAt = LocalDate.now();
    this.status = Status.PAID;
  }

  public record PaymentId(Long value) {}

  public enum Status {
    PENDING,
    PAID,
    OVERDUE
  }

  @Getter
  public enum Currency {
    PEN("S/"),
    USD("$"),
    EUR("€");
    private final String isoCode;

    Currency(String isoCode) {
      this.isoCode = isoCode;
    }
  }
}
