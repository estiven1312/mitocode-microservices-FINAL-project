package pe.com.peruapps.esignaturemicroservice.application.out;

public interface ContractSignedEventPort {

  void publish(ContractSignedEventCommand command);

}
