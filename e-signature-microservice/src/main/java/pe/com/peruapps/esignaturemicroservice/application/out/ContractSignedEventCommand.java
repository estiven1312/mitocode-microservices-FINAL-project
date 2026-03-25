package pe.com.peruapps.esignaturemicroservice.application.out;

import pe.com.peruapps.esignaturemicroservice.domain.model.Id;

public record ContractSignedEventCommand(
    Id contractId, String urlSignedFile, String hashSigned, String op) {
  public static ContractSignedEventCommand ofDelete(Id contractId) {
    return new ContractSignedEventCommand(contractId, null, null, "DELETE");
  }
}
