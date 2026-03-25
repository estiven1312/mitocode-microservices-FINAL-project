package pe.com.peruapps.accountabilitymicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.peruapps.accountabilitymicroservice.model.Currency;

import java.util.Optional;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {

  Optional<Currency> findByIsoCode(String isoCode);

  Optional<Currency> findByIso3Code(String iso3Code);
}
