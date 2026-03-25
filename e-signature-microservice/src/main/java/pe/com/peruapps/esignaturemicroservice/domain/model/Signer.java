package pe.com.peruapps.esignaturemicroservice.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Signer {
  private Id id;
  private String name;
  private String lastName;
  private String email;
  private String documentNumber;
}
