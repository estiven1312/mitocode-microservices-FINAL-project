package pe.com.peruapps.esignaturemicroservice.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = false)
public class Signature {

  private Id id;
  private Id contractId;
  private String urlDocumentSigned;
  private String hashDocument;
  private LocalDateTime signedAt;
  private SignatureStatus status;
  private String codeSignature;
  private String urlSignature;
  private String hashSignature;
  private LocalDateTime signatureCreatedAt;
  private String details;
  private Id signerId;

  public enum SignatureStatus {
    PENDING,
    SIGNED,
    REJECTED
  }
}
