package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DeliverablesByMilestoneJpaRepository
    extends JpaRepository<DeliverablesByMilestoneJpaEntity, Long> {

  @Modifying
  @Query("DELETE FROM DeliverablesByMilestoneJpaEntity dbm "
      + "WHERE dbm.contract.contractId = :contractId AND dbm.milestoneId = :milestoneId")
  void deleteByContractIdAndMilestoneId(@Param("contractId") UUID contractId,
                                        @Param("milestoneId") Long milestoneId);
}

