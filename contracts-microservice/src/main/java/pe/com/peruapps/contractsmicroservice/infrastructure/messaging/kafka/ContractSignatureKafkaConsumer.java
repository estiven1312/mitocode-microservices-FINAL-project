package pe.com.peruapps.contractsmicroservice.infrastructure.messaging.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pe.com.peruapps.contractsmicroservice.application.use_case.ApplyContractSignedEventUseCase;
import pe.com.peruapps.contractsmicroservice.application.use_case.RevertContractSignedEventUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContractSignatureKafkaConsumer {

  private final ApplyContractSignedEventUseCase applyContractSignedEventUseCase;
  private final RevertContractSignedEventUseCase revertContractSignedEventUseCase;

  @KafkaListener(
      topics = "${app.kafka.topics.contract-signed}",
      groupId = "${app.kafka.group-id}",
      properties = {
        "spring.json.use.type.headers=false",
        "spring.json.value.default.type=pe.com.peruapps.contractsmicroservice.infrastructure.messaging.kafka.ContractSignedKafkaEvent"
      })
  public void consumeContractSigned(ContractSignedKafkaEvent event) {
    log.info("Received ContractSignedKafkaEvent {}", event);
    if (event == null || event.contractId() == null) {
      log.warn("Ignoring contract-signed event because contractId is null");
      return;
    }
    if (event.op() == ContractSignedKafkaEvent.Operation.SIGN) {
      applyContractSignedEventUseCase.execute(
          new Contract.ContractId(event.contractId()), event.urlSignedFile(), event.hashSigned());
    } else if (event.op() == ContractSignedKafkaEvent.Operation.DELETE) {
      revertContractSignedEventUseCase.execute(new Contract.ContractId(event.contractId()));
    } else {
      log.warn(
          "Unknown operation {} in ContractSignedKafkaEvent for contractId {}",
          event.op(),
          event.contractId());
    }
  }
}
