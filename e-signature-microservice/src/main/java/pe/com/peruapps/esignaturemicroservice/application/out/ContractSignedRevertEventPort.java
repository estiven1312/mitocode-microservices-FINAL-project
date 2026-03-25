package pe.com.peruapps.esignaturemicroservice.application.out;

import pe.com.peruapps.esignaturemicroservice.domain.model.Id;

public interface ContractSignedRevertEventPort {

  void publish(ContractSignedEventCommand command);

}

