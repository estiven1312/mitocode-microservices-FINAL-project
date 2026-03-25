package pe.com.peruapps.esignaturemicroservice.application.in.usecase.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.peruapps.esignaturemicroservice.application.in.saga.SignatureSagaContext;
import pe.com.peruapps.esignaturemicroservice.application.in.saga.SignatureStep;
import pe.com.peruapps.esignaturemicroservice.application.in.usecase.SignContractCommand;
import pe.com.peruapps.esignaturemicroservice.application.in.usecase.SignatureContractUseCase;
import pe.com.peruapps.esignaturemicroservice.domain.errors.ErrorToSignatureException;
import pe.com.peruapps.esignaturemicroservice.domain.errors.SignatureNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignatureContractImpl implements SignatureContractUseCase {

  private final List<SignatureStep> signatureSteps;

  @Override
  public void execute(SignContractCommand signContractCommand) {
    List<SignatureStep> executedSteps = new ArrayList<>();
    SignatureSagaContext context =
        new SignatureSagaContext(
            signContractCommand.contractId(),
            signContractCommand.urlDocument(),
            signContractCommand.email(),
            signContractCommand.signerName(),
            signContractCommand.signerLastName(),
            signContractCommand.signerDni(),
            signContractCommand.signerPhone(),
            signContractCommand.signerEmail(),
            null,
            null);

    try {
      for (SignatureStep step : signatureSteps) {
        context = step.execute(context);
        executedSteps.add(step);
      }
    } catch (Exception e) {
      log.error(
          "Error executing signature saga for contractId: {}, error: {}",
          context.contractId().value(),
          e.getMessage(),
          e);
      for (SignatureStep step : executedSteps) {
        step.compensate(context);
      }
      throw new ErrorToSignatureException(
          "Failed to sign contract with id: " + signContractCommand.contractId().value());
    }
  }
}
