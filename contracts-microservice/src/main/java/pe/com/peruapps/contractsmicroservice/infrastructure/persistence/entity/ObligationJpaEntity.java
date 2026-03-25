package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import pe.com.peruapps.contractsmicroservice.domain.entity.Obligation;

import java.time.LocalDate;

@Entity
@Table(name = "obligations")
@Getter
@Setter
public class ObligationJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contract_id", nullable = false)
  private ContractJpaEntity contract;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private Obligation.ObligationStatus status;
}
