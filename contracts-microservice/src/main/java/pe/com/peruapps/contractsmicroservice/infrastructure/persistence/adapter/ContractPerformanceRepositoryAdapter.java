package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.adapter;

import pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pe.com.peruapps.contractsmicroservice.application.port.ContractPerformanceRepository;
import pe.com.peruapps.contractsmicroservice.domain.entity.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContractPerformanceRepositoryAdapter implements ContractPerformanceRepository {

  private final ContractJpaRepository contractJpaRepository;
  private final PaymentJpaRepository paymentJpaRepository;
  private final DeliverableJpaRepository deliverableJpaRepository;
  private final MilestoneJpaRepository milestoneJpaRepository;
  private final ObligationJpaRepository obligationJpaRepository;
  private final DeliverablesByMilestoneJpaRepository deliverablesByMilestoneJpaRepository;
  private final ContractPerformanceJpaMapper mapper;


  @Override
  public Deliverable saveDeliverable(Deliverable deliverable) {
    ContractJpaEntity contract = findContract(deliverable.getContractId());
    DeliverableJpaEntity saved = deliverableJpaRepository.save(
        mapper.toDeliverableEntity(deliverable, contract));
    return mapper.toDeliverableDomain(saved);
  }

  @Override
  public Milestone saveMilestone(Milestone milestone) {
    ContractJpaEntity contract = findContract(milestone.getContractId());
    MilestoneJpaEntity saved = milestoneJpaRepository.save(
        mapper.toMilestoneEntity(milestone, contract));
    return mapper.toMilestoneDomain(saved);
  }

  @Override
  public Obligation saveObligation(Obligation obligation) {
    ContractJpaEntity contract = findContract(obligation.getContractId());
    ObligationJpaEntity saved = obligationJpaRepository.save(
        mapper.toObligationEntity(obligation, contract));
    return mapper.toObligationDomain(saved);
  }

  @Override
  public Payment createPayment(Payment payment) {
    ContractJpaEntity contract = findContract(payment.getContractId());
    PaymentJpaEntity saved = paymentJpaRepository.save(
        mapper.toPaymentEntity(payment, contract));
    return mapper.toPaymentDomain(saved, contract.getContractId());
  }

  @Override
  public Payment savePayment(Payment payment) {
    PaymentJpaEntity entity = paymentJpaRepository
        .findById(payment.getId().value())
        .orElseThrow(() -> new IllegalArgumentException(
            "Payment not found with id: " + payment.getId().value()));
    entity.setPaidAt(payment.getPaidAt());
    entity.setStatus(payment.getStatus());
    return mapper.toPaymentDomain(paymentJpaRepository.save(entity),
        entity.getContract().getContractId());
  }

  @Override
  public Optional<ContractPerformance> findByContractId(Contract.ContractId contractId) {
    return contractJpaRepository.findById(contractId.value()).map(mapper::toDomain);
  }

  @Override
  public Optional<Payment> findPaymentByContractIdAndPaymentId(Contract.ContractId contractId,
                                                               Payment.PaymentId paymentId) {
    return paymentJpaRepository
        .findByContractIdAndId(contractId.value(), paymentId.value())
        .map(pe -> mapper.toPaymentDomain(pe, contractId.value()));
  }

  @Override
  public List<Deliverable> findDeliverablesByContractId(Contract.ContractId contractId) {
    return deliverableJpaRepository.findAllByContractId(contractId.value()).stream()
        .map(mapper::toDeliverableDomain)
        .toList();
  }

  @Override
  public Optional<Deliverable> findDeliverableByContractIdAndId(Contract.ContractId contractId,
                                                                Deliverable.DeliverableId deliverableId) {
    return deliverableJpaRepository
        .findByContractIdAndId(contractId.value(), deliverableId.value())
        .map(mapper::toDeliverableDomain);
  }

  @Override
  public List<Milestone> findMilestonesByContractId(Contract.ContractId contractId) {
    return milestoneJpaRepository.findAllByContractId(contractId.value()).stream()
        .map(mapper::toMilestoneDomain)
        .toList();
  }

  @Override
  public Optional<Milestone> findMilestoneByContractIdAndId(Contract.ContractId contractId,
                                                            Milestone.MilestoneId milestoneId) {
    return milestoneJpaRepository
        .findByContractIdAndId(contractId.value(), milestoneId.value())
        .map(mapper::toMilestoneDomain);
  }

  @Override
  public List<Obligation> findObligationsByContractId(Contract.ContractId contractId) {
    return obligationJpaRepository.findAllByContractId(contractId.value()).stream()
        .map(mapper::toObligationDomain)
        .toList();
  }

  @Override
  public Optional<Obligation> findObligationByContractIdAndId(Contract.ContractId contractId,
                                                              Obligation.ObligationId obligationId) {
    return obligationJpaRepository
        .findByContractIdAndId(contractId.value(), obligationId.value())
        .map(mapper::toObligationDomain);
  }

  @Override
  public List<Payment> findPaymentsByContractId(Contract.ContractId contractId) {
    return paymentJpaRepository.findAllByContractId(contractId.value()).stream()
        .map(pe -> mapper.toPaymentDomain(pe, contractId.value()))
        .toList();
  }

  @Override
  public void saveMilestoneDeliverableLinks(Contract.ContractId contractId,
                                            Milestone.MilestoneId milestoneId,
                                            List<Deliverable.DeliverableId> deliverableIds) {
    ContractJpaEntity contract = findContract(contractId);

    deliverablesByMilestoneJpaRepository.deleteByContractIdAndMilestoneId(
        contractId.value(), milestoneId.value());

    List<DeliverablesByMilestoneJpaEntity> rows = deliverableIds.stream()
        .distinct()
        .map(deliverableId -> mapper.toDeliverablesByMilestoneEntity(
            contract, milestoneId, deliverableId))
        .toList();

    deliverablesByMilestoneJpaRepository.saveAll(rows);
  }

  private ContractJpaEntity findContract(Contract.ContractId contractId) {
    return contractJpaRepository.findById(contractId.value())
        .orElseThrow(() -> new IllegalArgumentException(
            "Contract not found with id: " + contractId.value()));
  }
}
