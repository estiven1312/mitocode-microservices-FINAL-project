package pe.com.peruapps.esignaturemicroservice.infrastructure.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.peruapps.esignaturemicroservice.application.in.usecase.FindSignatureByContractUseCase;
import pe.com.peruapps.esignaturemicroservice.application.in.usecase.SignContractCommand;
import pe.com.peruapps.esignaturemicroservice.application.in.usecase.SignatureContractUseCase;
import pe.com.peruapps.esignaturemicroservice.domain.model.Id;

import java.util.UUID;

@RestController
@RequestMapping("/signature")
@RequiredArgsConstructor
public class SignatureController {

  private final SignatureContractUseCase signatureContractUseCase;
  private final FindSignatureByContractUseCase findSignatureByContractUseCase;

  @PostMapping("/contract/{contractId}")
  @PreAuthorize("hasAuthority('SIGN_CONTRACT')")
  public ResponseEntity<Void> signContract(
      @PathVariable String contractId,
      @RequestBody SignContractRequest request) {
    signatureContractUseCase.execute(
        new SignContractCommand(
            new Id(UUID.fromString(contractId)),
            request.urlDocument(),
            request.email(),
            request.signerName(),
            request.signerLastName(),
            request.signerDni(),
            request.signerPhone(),
            request.signerEmail()));

    return ResponseEntity.accepted().build();
  }

  @GetMapping("/contracts/{contractId}")
  public ResponseEntity<SignatureResponse> findByContractId(@PathVariable String contractId) {
    var signature = findSignatureByContractUseCase.findSignatureByContract(contractId);

    return ResponseEntity.ok(
        new SignatureResponse(
            signature.getId().value(),
            signature.getContractId().value(),
            signature.getUrlDocumentSigned(),
            signature.getHashDocument(),
            signature.getStatus() == null ? null : signature.getStatus().name(),
            signature.getSignedAt(),
            signature.getDetails()));
  }
}
