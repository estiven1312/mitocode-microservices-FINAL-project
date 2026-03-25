package pe.com.peruapps.esignaturemicroservice.application.in.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import pe.com.peruapps.esignaturemicroservice.application.out.ContractSignedEventCommand;
import pe.com.peruapps.esignaturemicroservice.application.out.ContractSignedEventPort;
import pe.com.peruapps.esignaturemicroservice.application.out.ContractSignedRevertEventPort;

@Service
@Slf4j
@Order(300)
@RequiredArgsConstructor
public class UpdateContractStatusStep implements SignatureStep {

  private final ContractSignedEventPort contractSignedEventPort;
  private final ContractSignedRevertEventPort contractSignedRevertEventPort;

  @Override
  public SignatureSagaContext execute(SignatureSagaContext context) {
    if (context.urlSignedFile() == null || context.hashSigned() == null) {
      throw new IllegalStateException("Signed file metadata is required to publish Kafka event");
    }

    contractSignedEventPort.publish(
        new ContractSignedEventCommand(
            context.contractId(), context.urlSignedFile(), context.hashSigned(), "SIGN"));

    log.info("Contract signed event published for contractId: {}", context.contractId().value());
    return context;
  }

  @Override
  public void compensate(SignatureSagaContext context) {
    contractSignedRevertEventPort.publish(
        ContractSignedEventCommand.ofDelete(context.contractId()));
    log.error(
        "Contract signed retry event published for contractId: {}", context.contractId().value());
  }
}
