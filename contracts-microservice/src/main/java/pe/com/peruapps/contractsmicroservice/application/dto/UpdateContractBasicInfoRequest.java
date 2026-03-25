package pe.com.peruapps.contractsmicroservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateContractBasicInfoRequest(
    @NotBlank String name,
    @NotBlank String description,
    @NotNull Contract.Type type,
    @NotNull Contract.ServiceType serviceType,
    Contract.ContractId relatedContractId,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    @NotNull Id.ThirdPartyId thirdPartyId,
    @NotNull Id.OrganizationalEntityId requestedArea,
    @NotNull Id.OrganizationalEntityId requestedCompany,
    @NotNull BigDecimal amount
) {}

