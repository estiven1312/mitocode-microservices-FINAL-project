package pe.com.peruapps.contractsmicroservice.application.dto;

import pe.com.peruapps.contractsmicroservice.domain.entity.Deliverable;
import pe.com.peruapps.contractsmicroservice.domain.entity.Milestone;
import pe.com.peruapps.contractsmicroservice.domain.entity.Obligation;
import pe.com.peruapps.contractsmicroservice.domain.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ContractPerformanceDtos {

  public record AddDeliverableRequest(
      String name,
      String description,
      LocalDate scheduleDate
  ) {}

  public record AddMilestoneRequest(
      String name,
      String description,
      LocalDate dueDate
  ) {}

  public record AddObligationRequest(
      String description,
      LocalDate dueDate
  ) {}

  public record AddPaymentRequest(
      String codePayment,
      BigDecimal amount,
      Payment.Currency currency,
      LocalDate dueDate
  ) {}

  public record MarkPaymentAsPaidRequest(
      Payment.PaymentId paymentId
  ) {}

  public record LinkDeliverablesRequest(
      Milestone.MilestoneId milestoneId,
      List<Deliverable.DeliverableId> deliverableIds
  ) {}

  public record DeliverableResponse(
      Deliverable.DeliverableId id,
      String name,
      String description,
      LocalDate scheduleDate,
      Deliverable.Status status
  ) {}


  public record MilestoneResponse(
      Milestone.MilestoneId id,
      String name,
      String description,
      LocalDate dueDate,
      Milestone.Status status,
      List<Deliverable.DeliverableId> linkedDeliverableIds
  ) {}

  public record ObligationResponse(
      Obligation.ObligationId id,
      String description,
      LocalDate dueDate,
      Obligation.ObligationStatus status
  ) {}

  public record PaymentResponse(
      Payment.PaymentId id,
      String codePayment,
      BigDecimal amount,
      Payment.Currency currency,
      LocalDate dueDate,
      LocalDate paidAt,
      Payment.Status status
  ) {}

  public record PerformanceResponse(
      List<DeliverableResponse> deliverables,
      List<MilestoneResponse> milestones,
      List<ObligationResponse> obligations,
      List<PaymentResponse> payments
  ) {}
}
