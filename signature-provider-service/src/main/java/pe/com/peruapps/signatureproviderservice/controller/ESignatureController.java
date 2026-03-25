package pe.com.peruapps.signatureproviderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.com.peruapps.signatureproviderservice.dto.ESignatureRequest;
import pe.com.peruapps.signatureproviderservice.dto.ESignatureResponse;
import pe.com.peruapps.signatureproviderservice.service.ESignatureService;

@RestController
@RequestMapping("/docu-sign")
@RequiredArgsConstructor
public class ESignatureController {

  private final ESignatureService eSignatureService;

  @PostMapping
  public ESignatureResponse signESignature(@RequestBody ESignatureRequest eSignatureRequest) {
    return eSignatureService.generateSignature(eSignatureRequest);
  }

  @DeleteMapping("/{contractCode}")
  public void deleteESignature(@PathVariable String contractCode) {
    eSignatureService.deleteSignature(contractCode);
  }
}
