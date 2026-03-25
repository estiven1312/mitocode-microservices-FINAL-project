package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MilestoneJpaRepository extends JpaRepository<MilestoneJpaEntity, Long> {

  @Query("SELECT m FROM MilestoneJpaEntity m WHERE m.contract.contractId = :contractId")
  List<MilestoneJpaEntity> findAllByContractId(@Param("contractId") UUID contractId);

  @Query("SELECT m FROM MilestoneJpaEntity m WHERE m.contract.contractId = :contractId AND m.id = :id")
  Optional<MilestoneJpaEntity> findByContractIdAndId(@Param("contractId") UUID contractId,
                                                     @Param("id") Long id);
}
