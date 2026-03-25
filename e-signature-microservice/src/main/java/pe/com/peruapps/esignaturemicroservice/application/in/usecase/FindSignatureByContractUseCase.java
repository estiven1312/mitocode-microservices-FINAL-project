package pe.com.peruapps.esignaturemicroservice.application.in.usecase;

import pe.com.peruapps.esignaturemicroservice.domain.model.Signature;

public interface FindSignatureByContractUseCase {
    Signature findSignatureByContract(String contractId);
}
