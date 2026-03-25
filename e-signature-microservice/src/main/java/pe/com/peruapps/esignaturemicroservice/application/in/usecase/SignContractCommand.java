package pe.com.peruapps.esignaturemicroservice.application.in.usecase;

import pe.com.peruapps.esignaturemicroservice.domain.model.Id;

public record SignContractCommand(
    Id contractId,
    String urlDocument,
    String email,
    String signerName,
    String signerLastName,
    String signerDni,
    String signerPhone,
    String signerEmail) {}
