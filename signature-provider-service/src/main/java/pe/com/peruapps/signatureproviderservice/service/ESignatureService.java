package pe.com.peruapps.signatureproviderservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.peruapps.signatureproviderservice.dto.ESignatureException;
import pe.com.peruapps.signatureproviderservice.dto.ESignatureRequest;
import pe.com.peruapps.signatureproviderservice.dto.ESignatureResponse;
import pe.com.peruapps.signatureproviderservice.dto.ESignatureStatus;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class ESignatureService {

  public ESignatureResponse generateSignature(ESignatureRequest request) {

    String codeSignature = "SIG-" + request.contractId(); // Generar un código de firma único
    String urlDocumentSigned =
        "https://example.com/signed-documents/"
            + codeSignature; // Simular la URL del documento firmado
    String hashSignature = generateHash(request.contractId(), codeSignature);
    String details =
        "Firma electrónica generada exitosamente, firmado por el usuario con correo "
            + request.email(); // Detalles adicionales

    return new ESignatureResponse(
        codeSignature,
        request.contractId(),
        ESignatureStatus.RESOLVED,
        urlDocumentSigned,
        hashSignature,
        details,
        LocalDateTime.now() // Timestamp actual
        );
  }

  public void deleteSignature(String codeSignature) {
    log.info("Deleting signature with code: {}", codeSignature);
  }

  private String generateHash(UUID contractId, String data) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
      StringBuilder hexString = new StringBuilder(2 * hash.length);
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new ESignatureException(
          contractId.toString(), "Error generating hash: " + e.getMessage());
    }
  }
}
