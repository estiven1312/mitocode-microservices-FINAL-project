package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, Long> {

  @Query("SELECT p FROM PaymentJpaEntity p WHERE p.contract.contractId = :contractId AND p.id = :paymentId")
  Optional<PaymentJpaEntity> findByContractIdAndId(@Param("contractId") UUID contractId,
                                                   @Param("paymentId") Long paymentId);

  @Query("SELECT p FROM PaymentJpaEntity p WHERE p.contract.contractId = :contractId")
  List<PaymentJpaEntity> findAllByContractId(@Param("contractId") UUID contractId);
}
