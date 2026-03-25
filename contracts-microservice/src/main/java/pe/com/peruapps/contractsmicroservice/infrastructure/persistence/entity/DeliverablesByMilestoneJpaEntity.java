package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "deliverables_by_milestone")
@Getter
@Setter
public class DeliverablesByMilestoneJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contract_id", nullable = false)
  private ContractJpaEntity contract;

  @Column(name = "milestone_id", nullable = false)
  private Long milestoneId;

  @Column(name = "deliverable_id", nullable = false)
  private Long deliverableId;
}
