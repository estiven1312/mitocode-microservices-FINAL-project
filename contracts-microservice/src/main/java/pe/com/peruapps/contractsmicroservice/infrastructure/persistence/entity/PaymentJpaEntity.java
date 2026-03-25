package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import pe.com.peruapps.contractsmicroservice.domain.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class PaymentJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contract_id", nullable = false)
  private ContractJpaEntity contract;

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal amount;

  @Column(name = "code_payment")
  private String codePayment;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private Payment.Currency currency;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Column(name = "paid_at")
  private LocalDate paidAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private Payment.Status status;
}
