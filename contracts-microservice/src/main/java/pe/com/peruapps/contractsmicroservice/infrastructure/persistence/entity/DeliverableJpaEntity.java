package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import pe.com.peruapps.contractsmicroservice.domain.entity.Deliverable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliverables")
@Getter
@Setter
public class DeliverableJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contract_id", nullable = false)
  private ContractJpaEntity contract;

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "schedule_date")
  private LocalDate scheduleDate;

  @Column(name = "delivered_at")
  private LocalDateTime deliveredAt;

  @Column(name = "accepted_at")
  private LocalDateTime acceptedAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private Deliverable.Status status;
}
