package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.adapter;

import pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity.*;

import org.springframework.stereotype.Component;
import pe.com.peruapps.contractsmicroservice.domain.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
class ContractPerformanceJpaMapper {

  ContractJpaEntity toEntity(ContractPerformance p, ContractJpaEntity entity) {
    entity.getDeliverables().clear();
    for (Deliverable d : p.getDeliverables()) {
      DeliverableJpaEntity de = new DeliverableJpaEntity();
      de.setContract(entity);
      de.setName(d.getName());
      de.setDescription(d.getDescription());
      de.setScheduleDate(d.getScheduleDate());
      de.setDeliveredAt(d.getDeliveredAt());
      de.setAcceptedAt(d.getAcceptedAt());
      de.setStatus(d.getStatus());
      entity.getDeliverables().add(de);
    }

    entity.getMilestones().clear();
    for (Milestone m : p.getMilestones()) {
      MilestoneJpaEntity me = new MilestoneJpaEntity();
      me.setContract(entity);
      me.setName(m.getName());
      me.setDescription(m.getDescription());
      me.setDueDate(m.getDueDate());
      me.setCompletedAt(m.getCompletedAt());
      me.setStatus(m.getStatus());
      entity.getMilestones().add(me);
    }

    entity.getObligations().clear();
    for (Obligation o : p.getObligations()) {
      ObligationJpaEntity oe = new ObligationJpaEntity();
      oe.setContract(entity);
      oe.setDescription(o.getDescription());
      oe.setDueDate(o.getDueDate());
      oe.setStatus(o.getStatus());
      entity.getObligations().add(oe);
    }

    entity.getPayments().clear();
    for (Payment pay : p.getPayments()) {
      PaymentJpaEntity pe = new PaymentJpaEntity();
      pe.setContract(entity);
      pe.setAmount(pay.getAmount());
      pe.setCodePayment(pay.getCodePayment());
      pe.setCurrency(pay.getCurrency());
      pe.setDueDate(pay.getDueDate());
      pe.setPaidAt(pay.getPaidAt());
      pe.setStatus(pay.getStatus());
      entity.getPayments().add(pe);
    }

    entity.getDeliverablesByMilestone().clear();
    for (ContractPerformance.DeliverablesByMilestone dbm : p.getDeliverablesByMilestone()) {
      for (Deliverable.DeliverableId deliverableId : dbm.getDeliverablesIds()) {
        DeliverablesByMilestoneJpaEntity row = new DeliverablesByMilestoneJpaEntity();
        row.setContract(entity);
        row.setMilestoneId(dbm.getMilestoneId().value());
        row.setDeliverableId(deliverableId.value());
        entity.getDeliverablesByMilestone().add(row);
      }
    }

    return entity;
  }

  ContractPerformance toDomain(ContractJpaEntity entity) {
    ContractPerformance p =
        ContractPerformance.forContract(new Contract.ContractId(entity.getContractId()));

    List<Deliverable> deliverables = new ArrayList<>();
    for (DeliverableJpaEntity de : entity.getDeliverables()) {
      deliverables.add(toDeliverableDomain(de));
    }
    p.setDeliverables(deliverables);

    List<Milestone> milestones = new ArrayList<>();
    for (MilestoneJpaEntity me : entity.getMilestones()) {
      milestones.add(toMilestoneDomain(me));
    }
    p.setMilestones(milestones);

    List<Obligation> obligations = new ArrayList<>();
    for (ObligationJpaEntity oe : entity.getObligations()) {
      obligations.add(toObligationDomain(oe));
    }
    p.setObligations(obligations);

    List<Payment> payments = new ArrayList<>();
    for (PaymentJpaEntity pe : entity.getPayments()) {
      payments.add(toPaymentDomain(pe, entity.getContractId()));
    }
    p.setPayments(payments);

    List<ContractPerformance.DeliverablesByMilestone> dbmList = new ArrayList<>();
    for (DeliverablesByMilestoneJpaEntity row : entity.getDeliverablesByMilestone()) {
      Milestone.MilestoneId milestoneId = new Milestone.MilestoneId(row.getMilestoneId());
      dbmList.stream()
          .filter(dbm -> dbm.getMilestoneId().equals(milestoneId))
          .findFirst()
          .ifPresentOrElse(
              existing ->
                  existing
                      .getDeliverablesIds()
                      .add(new Deliverable.DeliverableId(row.getDeliverableId())),
              () -> {
                ContractPerformance.DeliverablesByMilestone assoc =
                    new ContractPerformance.DeliverablesByMilestone();
                assoc.setMilestoneId(milestoneId);
                assoc.getDeliverablesIds().add(new Deliverable.DeliverableId(row.getDeliverableId()));
                dbmList.add(assoc);
              });
    }
    p.setDeliverablesByMilestone(dbmList);

    return p;
  }

  Payment toPaymentDomain(PaymentJpaEntity pe, UUID contractId) {
    Payment pay = new Payment();
    pay.setId(new Payment.PaymentId(pe.getId()));
    pay.setContractId(new Contract.ContractId(contractId));
    pay.setAmount(pe.getAmount());
    pay.setCodePayment(pe.getCodePayment());
    pay.setCurrency(pe.getCurrency());
    pay.setDueDate(pe.getDueDate());
    pay.setPaidAt(pe.getPaidAt());
    pay.setStatus(pe.getStatus());
    return pay;
  }

  Deliverable toDeliverableDomain(DeliverableJpaEntity de) {
    Deliverable d = new Deliverable();
    d.setId(new Deliverable.DeliverableId(de.getId()));
    d.setContractId(new Contract.ContractId(de.getContract().getContractId()));
    d.setName(de.getName());
    d.setDescription(de.getDescription());
    d.setScheduleDate(de.getScheduleDate());
    d.setDeliveredAt(de.getDeliveredAt());
    d.setAcceptedAt(de.getAcceptedAt());
    d.setStatus(de.getStatus());
    return d;
  }

  Milestone toMilestoneDomain(MilestoneJpaEntity me) {
    Milestone m = new Milestone();
    m.setId(new Milestone.MilestoneId(me.getId()));
    m.setContractId(new Contract.ContractId(me.getContract().getContractId()));
    m.setName(me.getName());
    m.setDescription(me.getDescription());
    m.setDueDate(me.getDueDate());
    m.setCompletedAt(me.getCompletedAt());
    m.setStatus(me.getStatus());
    return m;
  }

  Obligation toObligationDomain(ObligationJpaEntity oe) {
    Obligation o = new Obligation();
    o.setId(new Obligation.ObligationId(oe.getId()));
    o.setContractId(new Contract.ContractId(oe.getContract().getContractId()));
    o.setDescription(oe.getDescription());
    o.setDueDate(oe.getDueDate());
    o.setStatus(oe.getStatus());
    return o;
  }

  DeliverableJpaEntity toDeliverableEntity(Deliverable d, ContractJpaEntity contract) {
    DeliverableJpaEntity de = new DeliverableJpaEntity();
    if (d.getId() != null) {
      de.setId(d.getId().value());
    }
    de.setContract(contract);
    de.setName(d.getName());
    de.setDescription(d.getDescription());
    de.setScheduleDate(d.getScheduleDate());
    de.setDeliveredAt(d.getDeliveredAt());
    de.setAcceptedAt(d.getAcceptedAt());
    de.setStatus(d.getStatus());
    return de;
  }

  MilestoneJpaEntity toMilestoneEntity(Milestone m, ContractJpaEntity contract) {
    MilestoneJpaEntity me = new MilestoneJpaEntity();
    if (m.getId() != null) {
      me.setId(m.getId().value());
    }
    me.setContract(contract);
    me.setName(m.getName());
    me.setDescription(m.getDescription());
    me.setDueDate(m.getDueDate());
    me.setCompletedAt(m.getCompletedAt());
    me.setStatus(m.getStatus());
    return me;
  }

  ObligationJpaEntity toObligationEntity(Obligation o, ContractJpaEntity contract) {
    ObligationJpaEntity oe = new ObligationJpaEntity();
    if (o.getId() != null) {
      oe.setId(o.getId().value());
    }
    oe.setContract(contract);
    oe.setDescription(o.getDescription());
    oe.setDueDate(o.getDueDate());
    oe.setStatus(o.getStatus());
    return oe;
  }

  PaymentJpaEntity toPaymentEntity(Payment pay, ContractJpaEntity contract) {
    PaymentJpaEntity pe = new PaymentJpaEntity();
    if (pay.getId() != null) {
      pe.setId(pay.getId().value());
    }
    pe.setContract(contract);
    pe.setAmount(pay.getAmount());
    pe.setCodePayment(pay.getCodePayment());
    pe.setCurrency(pay.getCurrency());
    pe.setDueDate(pay.getDueDate());
    pe.setPaidAt(pay.getPaidAt());
    pe.setStatus(pay.getStatus());
    return pe;
  }

  DeliverablesByMilestoneJpaEntity toDeliverablesByMilestoneEntity(
      ContractJpaEntity contract,
      Milestone.MilestoneId milestoneId,
      Deliverable.DeliverableId deliverableId) {
    DeliverablesByMilestoneJpaEntity entity = new DeliverablesByMilestoneJpaEntity();
    entity.setContract(contract);
    entity.setMilestoneId(milestoneId.value());
    entity.setDeliverableId(deliverableId.value());
    return entity;
  }
}
