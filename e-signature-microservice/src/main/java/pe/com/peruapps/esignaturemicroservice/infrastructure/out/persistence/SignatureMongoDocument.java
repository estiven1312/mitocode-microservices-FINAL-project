package pe.com.peruapps.esignaturemicroservice.infrastructure.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import pe.com.peruapps.esignaturemicroservice.domain.model.Signature;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "signatures")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignatureMongoDocument {

  @Id private String id;

  @Indexed(unique = true)
  private UUID contractId;

  private String urlDocumentSigned;
  private String hashDocument;
  private LocalDateTime signedAt;
  private Signature.SignatureStatus status;
  private String codeSignature;
  private String urlSignature;
  private String hashSignature;
  private LocalDateTime signatureCreatedAt;
  private String details;
  private UUID signerId;
}

