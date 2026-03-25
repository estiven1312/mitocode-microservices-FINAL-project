package pe.com.peruapps.esignaturemicroservice.application.out;

import pe.com.peruapps.esignaturemicroservice.domain.model.Contract;
import pe.com.peruapps.esignaturemicroservice.domain.model.Id;

public interface ContractProviderPort {
  Contract findById(Id contractId);
}
