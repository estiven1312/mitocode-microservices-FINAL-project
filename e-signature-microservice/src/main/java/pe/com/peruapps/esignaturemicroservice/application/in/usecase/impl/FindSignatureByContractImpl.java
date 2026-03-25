package pe.com.peruapps.esignaturemicroservice.application.in.usecase.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.peruapps.esignaturemicroservice.application.in.usecase.FindSignatureByContractUseCase;
import pe.com.peruapps.esignaturemicroservice.application.out.SignaturePersistencePort;
import pe.com.peruapps.esignaturemicroservice.domain.model.Id;
import pe.com.peruapps.esignaturemicroservice.domain.model.Signature;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindSignatureByContractImpl implements FindSignatureByContractUseCase {

  private final SignaturePersistencePort signaturePersistencePort;

  @Override
  public Signature findSignatureByContract(String contractId) {
    return signaturePersistencePort.findByContractId(new Id(UUID.fromString(contractId)));
  }
}
