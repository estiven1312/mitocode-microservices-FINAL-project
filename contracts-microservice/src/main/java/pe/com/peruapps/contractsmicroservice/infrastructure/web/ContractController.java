package pe.com.peruapps.contractsmicroservice.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractResponse;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractStatusTransitionRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.CreateContractRequest;
import pe.com.peruapps.contractsmicroservice.application.dto.UpdateContractBasicInfoRequest;
import pe.com.peruapps.contractsmicroservice.application.use_case.CreateContractUseCase;
import pe.com.peruapps.contractsmicroservice.application.use_case.GetContractUseCase;
import pe.com.peruapps.contractsmicroservice.application.use_case.TransitionContractStatusUseCase;
import pe.com.peruapps.contractsmicroservice.application.use_case.UpdateContractBasicInfoUseCase;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractApproval;
import pe.com.peruapps.contractsmicroservice.domain.entity.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contracts")
@PreAuthorize("hasAuthority('CREATE_CONTRACT')")
@RequiredArgsConstructor
public class ContractController {

  private final CreateContractUseCase createContractUseCase;
  private final GetContractUseCase getContractUseCase;
  private final TransitionContractStatusUseCase transitionContractStatusUseCase;
  private final UpdateContractBasicInfoUseCase updateContractBasicInfoUseCase;

  @GetMapping("/{contractId}")
  public ResponseEntity<ContractView> getContract(@PathVariable UUID contractId) {
    return ResponseEntity.ok(
        toView(getContractUseCase.execute(new Contract.ContractId(contractId))));
  }

  @PostMapping
  public ResponseEntity<ContractView> createContract(@Valid @RequestBody ContractRequest request) {
    CreateContractRequest useCaseRequest =
        new CreateContractRequest(
            request.name(),
            request.description(),
            request.type(),
            request.serviceType(),
            request.relatedContractId() != null
                ? new Contract.ContractId(request.relatedContractId())
                : null,
            request.startDate(),
            request.endDate(),
            new Id.ThirdPartyId(request.thirdPartyId()),
            new Id.UserId(request.createdBy()),
            new Id.OrganizationalEntityId(request.requestedArea()),
            new Id.OrganizationalEntityId(request.requestedCompany()),
            request.amount(),
            request.urlFile());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(toView(createContractUseCase.execute(useCaseRequest)));
  }

  /**
   * PATCH /api/v1/contracts/{contractId}/status Transitions the contract to the next status based
   * on the provided action.
   */
  @PatchMapping("/{contractId}/status")
  public ResponseEntity<ContractView> transitionStatus(
      @PathVariable UUID contractId, @Valid @RequestBody StatusTransitionRequest request) {
    ContractStatusTransitionRequest useCaseRequest =
        new ContractStatusTransitionRequest(
            request.action(),
            request.managerId() != null ? new Id.UserId(request.managerId()) : null,
            request.approvalId() != null
                ? new ContractApproval.ApprovalId(request.approvalId())
                : null,
            request.approverId() != null ? new Id.UserId(request.approverId()) : null,
            request.approverEmail(),
            request.approverName(),
            request.comment());
    return ResponseEntity.ok(
        toView(
            transitionContractStatusUseCase.execute(
                new Contract.ContractId(contractId), useCaseRequest)));
  }

  @PutMapping("/{contractId}/basic-info")
  public ResponseEntity<ContractView> updateBasicInfo(
      @PathVariable UUID contractId,
      @Valid @RequestBody UpdateContractBasicInfoWebRequest request) {
    UpdateContractBasicInfoRequest useCaseRequest =
        new UpdateContractBasicInfoRequest(
            request.name(),
            request.description(),
            request.type(),
            request.serviceType(),
            request.relatedContractId() != null
                ? new Contract.ContractId(request.relatedContractId())
                : null,
            request.startDate(),
            request.endDate(),
            new Id.ThirdPartyId(request.thirdPartyId()),
            new Id.OrganizationalEntityId(request.requestedArea()),
            new Id.OrganizationalEntityId(request.requestedCompany()),
            request.amount());

    return ResponseEntity.ok(
        toView(
            updateContractBasicInfoUseCase.execute(
                new Contract.ContractId(contractId), useCaseRequest)));
  }

  // --- Web-layer records (raw UUID / primitives only) ---

  public record ContractRequest(
      String name,
      String description,
      Contract.Type type,
      Contract.ServiceType serviceType,
      UUID relatedContractId,
      LocalDate startDate,
      LocalDate endDate,
      UUID thirdPartyId,
      UUID createdBy,
      UUID requestedArea,
      UUID requestedCompany,
      BigDecimal amount,
      String urlFile) {}

  public record StatusTransitionRequest(
      ContractStatusTransitionRequest.ContractAction action,
      UUID managerId,
      Long approvalId,
      UUID approverId,
      String approverEmail,
      String approverName,
      String comment) {}

  public record ContractView(
      UUID contractId,
      String name,
      String description,
      Contract.Type type,
      Contract.ServiceType serviceType,
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
      Contract.Status status,
      List<ApprovalView> approvals) {}

  public record ApprovalView(
      Long approvalId,
      UUID approverId,
      String approverEmail,
      String approverName,
      String comment,
      ContractApproval.Status status) {}

  public record UpdateContractBasicInfoWebRequest(
      String name,
      String description,
      Contract.Type type,
      Contract.ServiceType serviceType,
      UUID relatedContractId,
      LocalDate startDate,
      LocalDate endDate,
      UUID thirdPartyId,
      UUID requestedArea,
      UUID requestedCompany,
      BigDecimal amount) {}

  // --- Mapping from application ContractResponse → web ContractView ---

  private ContractView toView(ContractResponse r) {
    List<ApprovalView> approvalViews =
        r.approvals().stream()
            .map(
                a ->
                    new ApprovalView(
                        a.approvalId() != null ? a.approvalId().getValue() : null,
                        a.approverId() != null ? a.approverId().getValue() : null,
                        a.approverEmail(),
                        a.approverName(),
                        a.comment(),
                        a.status()))
            .toList();

    return new ContractView(
        r.contractId().value(),
        r.name(),
        r.description(),
        r.type(),
        r.serviceType(),
        r.relatedContractId() != null ? r.relatedContractId().value() : null,
        r.startDate(),
        r.endDate(),
        r.thirdPartyId().getValue(),
        r.createdBy().getValue(),
        r.createdAt(),
        r.requestedArea().getValue(),
        r.requestedCompany().getValue(),
        r.amount(),
        r.urlFile(),
        r.hashSignature(),
        r.urlSignedFile(),
        r.status(),
        approvalViews);
  }
}
