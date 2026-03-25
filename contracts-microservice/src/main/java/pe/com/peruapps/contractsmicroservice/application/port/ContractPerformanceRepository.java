package pe.com.peruapps.contractsmicroservice.application.port;

import pe.com.peruapps.contractsmicroservice.domain.entity.*;

import java.util.List;
import java.util.Optional;

public interface ContractPerformanceRepository {

  Deliverable saveDeliverable(Deliverable deliverable);

  Milestone saveMilestone(Milestone milestone);

  Obligation saveObligation(Obligation obligation);

  Payment createPayment(Payment payment);

  Payment savePayment(Payment payment);

  void saveMilestoneDeliverableLinks(Contract.ContractId contractId,
                                     Milestone.MilestoneId milestoneId,
                                     List<Deliverable.DeliverableId> deliverableIds);

  Optional<ContractPerformance> findByContractId(Contract.ContractId contractId);

  Optional<Payment> findPaymentByContractIdAndPaymentId(Contract.ContractId contractId,
                                                         Payment.PaymentId paymentId);

  List<Deliverable> findDeliverablesByContractId(Contract.ContractId contractId);

  Optional<Deliverable> findDeliverableByContractIdAndId(Contract.ContractId contractId,
                                                          Deliverable.DeliverableId deliverableId);

  List<Milestone> findMilestonesByContractId(Contract.ContractId contractId);

  Optional<Milestone> findMilestoneByContractIdAndId(Contract.ContractId contractId,
                                                      Milestone.MilestoneId milestoneId);

  List<Obligation> findObligationsByContractId(Contract.ContractId contractId);

  Optional<Obligation> findObligationByContractIdAndId(Contract.ContractId contractId,
                                                        Obligation.ObligationId obligationId);

  List<Payment> findPaymentsByContractId(Contract.ContractId contractId);
}
