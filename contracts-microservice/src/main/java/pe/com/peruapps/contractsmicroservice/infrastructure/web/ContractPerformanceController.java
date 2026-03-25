package pe.com.peruapps.contractsmicroservice.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.*;
import pe.com.peruapps.contractsmicroservice.application.use_case.*;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.Deliverable;
import pe.com.peruapps.contractsmicroservice.domain.entity.Milestone;
import pe.com.peruapps.contractsmicroservice.domain.entity.Obligation;
import pe.com.peruapps.contractsmicroservice.domain.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contract-performance/{contractId}")
@PreAuthorize("hasAuthority('CREATE_CONTRACT')")
@RequiredArgsConstructor
public class ContractPerformanceController {

  private final AddDeliverableUseCase addDeliverableUseCase;
  private final AddMilestoneUseCase addMilestoneUseCase;
  private final AddObligationUseCase addObligationUseCase;
  private final AddPaymentUseCase addPaymentUseCase;
  private final LinkDeliverablesUseCase linkDeliverablesUseCase;
  private final MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase;
  private final GetContractPerformanceUseCase getContractPerformanceUseCase;
  private final GetDeliverablesUseCase getDeliverablesUseCase;
  private final GetDeliverableUseCase getDeliverableUseCase;
  private final GetMilestonesUseCase getMilestonesUseCase;
  private final GetMilestoneUseCase getMilestoneUseCase;
  private final GetObligationsUseCase getObligationsUseCase;
  private final GetObligationUseCase getObligationUseCase;
  private final GetPaymentsUseCase getPaymentsUseCase;
  private final GetPaymentUseCase getPaymentUseCase;

  @GetMapping
  public ResponseEntity<PerformanceView> getPerformance(@PathVariable UUID contractId) {
    PerformanceResponse r = getContractPerformanceUseCase.execute(new Contract.ContractId(contractId));
    return ResponseEntity.ok(new PerformanceView(
        r.deliverables().stream().map(this::toDeliverableView).toList(),
        r.milestones().stream().map(this::toMilestoneView).toList(),
        r.obligations().stream().map(this::toObligationView).toList(),
        r.payments().stream().map(this::toPaymentView).toList()
    ));
  }

  @GetMapping("/deliverables")
  public ResponseEntity<List<DeliverableView>> getDeliverables(@PathVariable UUID contractId) {
    return ResponseEntity.ok(
        getDeliverablesUseCase.execute(new Contract.ContractId(contractId)).stream()
            .map(this::toDeliverableView).toList());
  }

  @GetMapping("/deliverables/{deliverableId}")
  public ResponseEntity<DeliverableView> getDeliverable(@PathVariable UUID contractId,
                                                        @PathVariable Long deliverableId) {
    return ResponseEntity.ok(toDeliverableView(
        getDeliverableUseCase.execute(new Contract.ContractId(contractId),
            new Deliverable.DeliverableId(deliverableId))));
  }

  @GetMapping("/milestones")
  public ResponseEntity<List<MilestoneView>> getMilestones(@PathVariable UUID contractId) {
    return ResponseEntity.ok(
        getMilestonesUseCase.execute(new Contract.ContractId(contractId)).stream()
            .map(this::toMilestoneView).toList());
  }

  @GetMapping("/milestones/{milestoneId}")
  public ResponseEntity<MilestoneView> getMilestone(@PathVariable UUID contractId,
                                                    @PathVariable Long milestoneId) {
    return ResponseEntity.ok(toMilestoneView(
        getMilestoneUseCase.execute(new Contract.ContractId(contractId),
            new Milestone.MilestoneId(milestoneId))));
  }

  @GetMapping("/obligations")
  public ResponseEntity<List<ObligationView>> getObligations(@PathVariable UUID contractId) {
    return ResponseEntity.ok(
        getObligationsUseCase.execute(new Contract.ContractId(contractId)).stream()
            .map(this::toObligationView).toList());
  }

  @GetMapping("/obligations/{obligationId}")
  public ResponseEntity<ObligationView> getObligation(@PathVariable UUID contractId,
                                                      @PathVariable Long obligationId) {
    return ResponseEntity.ok(toObligationView(
        getObligationUseCase.execute(new Contract.ContractId(contractId),
            new Obligation.ObligationId(obligationId))));
  }

  @GetMapping("/payments")
  public ResponseEntity<List<PaymentView>> getPayments(@PathVariable UUID contractId) {
    return ResponseEntity.ok(
        getPaymentsUseCase.execute(new Contract.ContractId(contractId)).stream()
            .map(this::toPaymentView).toList());
  }

  @GetMapping("/payments/{paymentId}")
  public ResponseEntity<PaymentView> getPayment(@PathVariable UUID contractId,
                                                @PathVariable Long paymentId) {
    return ResponseEntity.ok(toPaymentView(
        getPaymentUseCase.execute(new Contract.ContractId(contractId),
            new Payment.PaymentId(paymentId))));
  }

  @PostMapping("/deliverables")
  public ResponseEntity<DeliverableView> addDeliverable(
      @PathVariable UUID contractId,
      @Valid @RequestBody AddDeliverableWebRequest request) {
    DeliverableResponse response = addDeliverableUseCase.execute(
        new Contract.ContractId(contractId),
        new AddDeliverableRequest(request.name(), request.description(), request.scheduleDate()));
    return ResponseEntity.status(HttpStatus.CREATED).body(toDeliverableView(response));
  }

  @PostMapping("/milestones")
  public ResponseEntity<MilestoneView> addMilestone(
      @PathVariable UUID contractId,
      @Valid @RequestBody AddMilestoneWebRequest request) {
    MilestoneResponse response = addMilestoneUseCase.execute(
        new Contract.ContractId(contractId),
        new AddMilestoneRequest(request.name(), request.description(), request.dueDate()));
    return ResponseEntity.status(HttpStatus.CREATED).body(toMilestoneView(response));
  }

  @PostMapping("/obligations")
  public ResponseEntity<ObligationView> addObligation(
      @PathVariable UUID contractId,
      @Valid @RequestBody AddObligationWebRequest request) {
    ObligationResponse response = addObligationUseCase.execute(
        new Contract.ContractId(contractId),
        new AddObligationRequest(request.description(), request.dueDate()));
    return ResponseEntity.status(HttpStatus.CREATED).body(toObligationView(response));
  }

  @PostMapping("/payments")
  public ResponseEntity<PaymentView> addPayment(
      @PathVariable UUID contractId,
      @Valid @RequestBody AddPaymentWebRequest request) {
    PaymentResponse response = addPaymentUseCase.execute(
        new Contract.ContractId(contractId),
        new AddPaymentRequest(request.codePayment(), request.amount(), request.currency(), request.dueDate()));
    return ResponseEntity.status(HttpStatus.CREATED).body(toPaymentView(response));
  }

  @PatchMapping("/payments/{paymentId}/paid")
  public ResponseEntity<PaymentView> markAsPaid(
      @PathVariable UUID contractId,
      @PathVariable Long paymentId) {
    PaymentResponse response = markPaymentAsPaidUseCase.execute(
        new Contract.ContractId(contractId),
        new MarkPaymentAsPaidRequest(new Payment.PaymentId(paymentId)));
    return ResponseEntity.ok(toPaymentView(response));
  }

  @PostMapping("/milestones/{milestoneId}/deliverables")
  public ResponseEntity<MilestoneView> linkDeliverables(
      @PathVariable UUID contractId,
      @PathVariable Long milestoneId,
      @RequestBody List<Long> deliverableIds) {
    LinkDeliverablesRequest request = new LinkDeliverablesRequest(
        new Milestone.MilestoneId(milestoneId),
        deliverableIds.stream().map(Deliverable.DeliverableId::new).toList()
    );
    MilestoneResponse response = linkDeliverablesUseCase.execute(
        new Contract.ContractId(contractId), request);
    return ResponseEntity.ok(toMilestoneView(response));
  }


  public record PerformanceView(List<DeliverableView> deliverables,
                                List<MilestoneView> milestones,
                                List<ObligationView> obligations,
                                List<PaymentView> payments) {}

  public record AddDeliverableWebRequest(String name, String description, LocalDate scheduleDate) {}
  public record AddMilestoneWebRequest(String name, String description, LocalDate dueDate) {}
  public record AddObligationWebRequest(String description, LocalDate dueDate) {}
  public record AddPaymentWebRequest(String codePayment, BigDecimal amount, Payment.Currency currency, LocalDate dueDate) {}

  public record DeliverableView(Long id, String name, String description,
                                LocalDate scheduleDate, Deliverable.Status status) {}
  public record MilestoneView(Long id, String name, String description,
                              LocalDate dueDate, Milestone.Status status,
                              List<Long> linkedDeliverableIds) {}
  public record ObligationView(Long id, String description, LocalDate dueDate, String status) {}
  public record PaymentView(Long id, String codePayment, BigDecimal amount, Payment.Currency currency,
                            LocalDate dueDate, LocalDate paidAt, Payment.Status status) {}


  private DeliverableView toDeliverableView(DeliverableResponse r) {
    return new DeliverableView(
        r.id() != null ? r.id().value() : null,
        r.name(), r.description(), r.scheduleDate(), r.status());
  }

  private MilestoneView toMilestoneView(MilestoneResponse r) {
    return new MilestoneView(
        r.id() != null ? r.id().value() : null,
        r.name(), r.description(), r.dueDate(), r.status(),
        r.linkedDeliverableIds().stream()
            .map(Deliverable.DeliverableId::value)
            .toList());
  }

  private ObligationView toObligationView(ObligationResponse r) {
    return new ObligationView(
        r.id() != null ? r.id().value() : null,
        r.description(), r.dueDate(),
        r.status() != null ? r.status().name() : null);
  }

  private PaymentView toPaymentView(PaymentResponse r) {
    return new PaymentView(
        r.id() != null ? r.id().value() : null,
        r.codePayment(),
        r.amount(), r.currency(), r.dueDate(), r.paidAt(), r.status());
  }
}
