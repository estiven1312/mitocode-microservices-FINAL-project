package pe.com.peruapps.esignaturemicroservice.infrastructure.out.rest.client.contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContractResponse(
    UUID contractId,
    String name,
    String description,
    String type,
    String serviceType,
    UUID relatedContractId,
    LocalDate startDate,
    LocalDate endDate,
    UUID thirdPartyId,
    UUID createdBy,
    LocalDate createdAt,
    UUID requestedArea,
    UUID requestedCompany,
    BigDecimal amount,
    String urlFile,
    String hashSignature,
    String urlSignedFile,
    String status) {}
