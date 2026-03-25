package pe.com.peruapps.esignaturemicroservice.infrastructure.out.messaging.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pe.com.peruapps.esignaturemicroservice.application.out.ContractSignedEventCommand;
import pe.com.peruapps.esignaturemicroservice.application.out.ContractSignedRevertEventPort;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaContractSignedRevertEventAdapter implements ContractSignedRevertEventPort {

  private final KafkaTemplate<String, ContractSignedKafkaEvent> kafkaTemplate;

  @Value("${app.kafka.topics.contract-signed}")
  private String contractSignedTopic;

  @Override
  public void publish(ContractSignedEventCommand command) {
    var event =
            new ContractSignedKafkaEvent(
                    command.contractId().value(),
                    command.urlSignedFile(),
                    command.hashSigned(),
                    command.op());

    kafkaTemplate.send(contractSignedTopic, command.contractId().value().toString(), event);
    log.info(
        "Kafka retry event sent to topic {} for contractId: {}",
            contractSignedTopic,
        command.contractId().value());
  }
}
