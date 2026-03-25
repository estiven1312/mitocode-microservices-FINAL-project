package pe.com.peruapps.accountabilitymicroservice.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String codePayment;
  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;
  private BigDecimal amount;
  @ManyToOne(optional = false)
  @JoinColumn(name = "currency_id")
  private Currency currency;
  private LocalDateTime timestamp;
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private Status status;

  public enum Status {
    PENDING,
    APPROVED,
    REJECTED,
    NOT_PROCESSED
  }

}
