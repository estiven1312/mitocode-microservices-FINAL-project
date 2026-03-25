package pe.com.peruapps.accountabilitymicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.com.peruapps.accountabilitymicroservice.model.Payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

  @Query(
      value =
        """
        SELECT p
        FROM Payment p
        WHERE p.status::text = :status
        """,
      nativeQuery = true)
  List<Payment> findByStatus(String status);

  Optional<Payment> findByCodePayment(String codePayment);
}
