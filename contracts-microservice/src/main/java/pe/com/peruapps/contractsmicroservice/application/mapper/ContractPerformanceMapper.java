package pe.com.peruapps.contractsmicroservice.application.mapper;

import org.springframework.stereotype.Component;
import pe.com.peruapps.contractsmicroservice.application.dto.ContractPerformanceDtos.*;
import pe.com.peruapps.contractsmicroservice.domain.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContractPerformanceMapper {

  public Deliverable toDeliverableDomain(AddDeliverableRequest request) {
    Deliverable d = new Deliverable();
    d.setName(request.name());
    d.setDescription(request.description());
    d.setScheduleDate(request.scheduleDate());
    return d;
  }

  public Milestone toMilestoneDomain(AddMilestoneRequest request) {
    Milestone m = new Milestone();
    m.setName(request.name());
    m.setDescription(request.description());
    m.setDueDate(request.dueDate());
    return m;
  }

  public Obligation toObligationDomain(AddObligationRequest request) {
    Obligation o = new Obligation();
    o.setDescription(request.description());
    o.setDueDate(request.dueDate());
    return o;
  }

  public Payment toPaymentDomain(AddPaymentRequest request) {
    Payment p = new Payment();
    p.setCodePayment(request.codePayment());
    p.setAmount(request.amount());
    p.setCurrency(request.currency());
    p.setDueDate(request.dueDate());
    return p;
  }

  public DeliverableResponse toDeliverableResponse(Deliverable d) {
    return new DeliverableResponse(
        d.getId(),
        d.getName(),
        d.getDescription(),
        d.getScheduleDate(),
        d.getStatus()
    );
  }

  public MilestoneResponse toMilestoneResponse(Milestone m) {
    return toMilestoneResponse(m, null);
  }

  public MilestoneResponse toMilestoneResponse(Milestone m,
                                               ContractPerformance performance) {
    List<Deliverable.DeliverableId> linked = performance != null
        ? performance.getDeliverablesByMilestone().stream()
            .filter(dbm -> dbm.getMilestoneId().equals(m.getId()))
            .flatMap(dbm -> dbm.getDeliverablesIds().stream())
            .collect(Collectors.toList())
        : List.of();

    return new MilestoneResponse(
        m.getId(),
        m.getName(),
        m.getDescription(),
        m.getDueDate(),
        m.getStatus(),
        linked
    );
  }

  public ObligationResponse toObligationResponse(Obligation o) {
    return new ObligationResponse(
        o.getId(),
        o.getDescription(),
        o.getDueDate(),
        o.getStatus()
    );
  }

  public PaymentResponse toPaymentResponse(Payment p) {
    return new PaymentResponse(
        p.getId(),
        p.getCodePayment(),
        p.getAmount(),
        p.getCurrency(),
        p.getDueDate(),
        p.getPaidAt(),
        p.getStatus()
    );
  }
}
