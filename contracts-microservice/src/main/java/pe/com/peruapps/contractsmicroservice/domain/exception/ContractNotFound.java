package pe.com.peruapps.contractsmicroservice.domain.exception;

import lombok.Getter;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

@Getter
public class ContractNotFound extends RuntimeException {
  private final Contract.ContractId id;

  public ContractNotFound(Contract.ContractId id, String message) {
    super(message);
    this.id = id;
  }
}
