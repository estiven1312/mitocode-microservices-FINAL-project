package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliverableJpaRepository extends JpaRepository<DeliverableJpaEntity, Long> {

  @Query("SELECT d FROM DeliverableJpaEntity d WHERE d.contract.contractId = :contractId")
  List<DeliverableJpaEntity> findAllByContractId(@Param("contractId") UUID contractId);

  @Query("SELECT d FROM DeliverableJpaEntity d WHERE d.contract.contractId = :contractId AND d.id = :id")
  Optional<DeliverableJpaEntity> findByContractIdAndId(@Param("contractId") UUID contractId,
                                                       @Param("id") Long id);
}
