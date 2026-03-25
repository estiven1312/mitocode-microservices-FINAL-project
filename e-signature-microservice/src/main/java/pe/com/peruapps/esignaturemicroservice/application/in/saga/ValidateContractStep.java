package pe.com.peruapps.esignaturemicroservice.application.in.saga;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import pe.com.peruapps.esignaturemicroservice.application.out.ContractProviderPort;

@Service
@Slf4j
@Order(100)
public class ValidateContractStep implements SignatureStep {

  private final ContractProviderPort contractProviderPort;

  public ValidateContractStep(ContractProviderPort contractProviderPort) {
    this.contractProviderPort = contractProviderPort;
  }

  @Override
  public SignatureSagaContext execute(SignatureSagaContext context) {
    var contract = contractProviderPort.findById(context.contractId());
    if (!contract.isValidToSign()) {
      throw new IllegalStateException("Contract is not valid to sign");
    }
    return context;
  }

  @Override
  public void compensate(SignatureSagaContext context) {
    log.error("Compensating ValidateContractStep for contractId: {}", context.contractId().value());
  }

}
