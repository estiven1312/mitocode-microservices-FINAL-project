  package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

  import jakarta.persistence.*;
  import lombok.Getter;
  import lombok.Setter;
  import org.hibernate.annotations.JdbcType;
  import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
  import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

  import java.math.BigDecimal;
  import java.time.LocalDate;
  import java.util.ArrayList;
  import java.util.List;
  import java.util.UUID;

  @Entity
  @Table(name = "contracts")
  @Getter
  @Setter
  public class ContractJpaEntity {

    @Id
    @Column(name = "contract_id")
    private UUID contractId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private Contract.Type type;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private Contract.ServiceType serviceType;

    @Column(name = "related_contract_id")
    private UUID relatedContractId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "third_party_id", nullable = false)
    private UUID thirdPartyId;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "requested_area", nullable = false)
    private UUID requestedArea;

    @Column(name = "requested_company", nullable = false)
    private UUID requestedCompany;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Contract.Status status;

    @Column(name = "hash_signature")
    private String hashSignature;

    @Column(name = "url_file")
    private String urlFile;

    @Column(name = "url_signed_file")
    private String urlSignedFile;

    @OneToMany(mappedBy = "contract", fetch =  FetchType.LAZY)
    private List<ContractApprovalJpaEntity> approvals = new ArrayList<>();

    @OneToMany(mappedBy = "contract", fetch =  FetchType.LAZY)
    private List<DeliverableJpaEntity> deliverables = new ArrayList<>();

    @OneToMany(mappedBy = "contract", fetch =  FetchType.LAZY)
    private List<MilestoneJpaEntity> milestones = new ArrayList<>();

    @OneToMany(mappedBy = "contract", fetch =  FetchType.LAZY)
    private List<ObligationJpaEntity> obligations = new ArrayList<>();

    @OneToMany(mappedBy = "contract", fetch =  FetchType.LAZY)
    private List<PaymentJpaEntity> payments = new ArrayList<>();

    @OneToMany(mappedBy = "contract", fetch =  FetchType.LAZY)
    private List<DeliverablesByMilestoneJpaEntity> deliverablesByMilestone = new ArrayList<>();
  }
