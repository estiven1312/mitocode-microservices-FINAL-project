package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractApproval;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contract_approvals")
@Getter
@Setter
public class ContractApprovalJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contract_id", nullable = false)
  private ContractJpaEntity contract;

  @Column(name = "approver_id")
  private UUID approverId;

  @Column(name = "approver_email")
  private String approverEmail;

  @Column(name = "approver_name")
  private String approverName;

  @Column(columnDefinition = "TEXT")
  private String comment;

  @Column(name = "approval_date")
  private LocalDateTime approvalDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ContractApproval.Status status;
}
