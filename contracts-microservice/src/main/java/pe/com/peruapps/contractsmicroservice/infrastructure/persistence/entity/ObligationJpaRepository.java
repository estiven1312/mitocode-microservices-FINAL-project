package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObligationJpaRepository extends JpaRepository<ObligationJpaEntity, Long> {

  @Query("SELECT o FROM ObligationJpaEntity o WHERE o.contract.contractId = :contractId")
  List<ObligationJpaEntity> findAllByContractId(@Param("contractId") UUID contractId);

  @Query("SELECT o FROM ObligationJpaEntity o WHERE o.contract.contractId = :contractId AND o.id = :id")
  Optional<ObligationJpaEntity> findByContractIdAndId(@Param("contractId") UUID contractId,
                                                      @Param("id") Long id);
}
