package pe.com.peruapps.accountabilitymicroservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Currency {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String isoCode;
  private String iso3Code;
  private Boolean active;

  public enum CurrencyCode {
    PEN("S/"),
    USD("$"),
    EUR("€");
    private String isoCode;

    CurrencyCode(String isoCode) {
      this.isoCode = isoCode;
    }
    public String getIsoCode() {
      return isoCode;
    }
  }
}
