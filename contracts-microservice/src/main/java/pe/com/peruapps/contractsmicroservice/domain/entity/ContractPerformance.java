package pe.com.peruapps.contractsmicroservice.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ContractPerformance {

  private Contract.ContractId contractId;
  private List<Deliverable> deliverables = new ArrayList<>();
  private List<Milestone> milestones = new ArrayList<>();
  private List<Obligation> obligations = new ArrayList<>();
  private List<Payment> payments = new ArrayList<>();
  private List<DeliverablesByMilestone> deliverablesByMilestone = new ArrayList<>();


  public static ContractPerformance forContract(Contract.ContractId contractId) {
    ContractPerformance performance = new ContractPerformance();
    performance.contractId = contractId;
    return performance;
  }

  public void linkDeliveablesToMilestone(Milestone.MilestoneId milestoneId,
                                         List<Deliverable.DeliverableId> deliverableIds) {
    boolean milestoneExists = milestones.stream()
        .anyMatch(m -> m.getId().equals(milestoneId));
    if (!milestoneExists) {
      throw new IllegalArgumentException(
          "Milestone not found in this contract performance: " + milestoneId.value());
    }

    for (Deliverable.DeliverableId deliverableId : deliverableIds) {
      boolean deliverableExists = deliverables.stream()
          .anyMatch(d -> d.getId().equals(deliverableId));
      if (!deliverableExists) {
        throw new IllegalArgumentException(
            "Deliverable not found in this contract performance: " + deliverableId.value());
      }
    }

    deliverablesByMilestone.stream()
        .filter(dbm -> dbm.getMilestoneId().equals(milestoneId))
        .findFirst()
        .ifPresentOrElse(
            existing -> existing.getDeliverablesIds().addAll(deliverableIds),
            () -> {
              DeliverablesByMilestone assoc = new DeliverablesByMilestone();
              assoc.setMilestoneId(milestoneId);
              assoc.setDeliverablesIds(new ArrayList<>(deliverableIds));
              deliverablesByMilestone.add(assoc);
            });
  }


  @Getter
  @Setter
  public static class DeliverablesByMilestone {
    private Milestone.MilestoneId milestoneId;
    private List<Deliverable.DeliverableId> deliverablesIds = new ArrayList<>();
  }

  public void verifyMilestoneIsCompleted() {
    for (var dbm : deliverablesByMilestone) {
      var milestone =
          milestones.stream()
              .filter(m -> m.getId().equals(dbm.getMilestoneId()))
              .findFirst()
              .orElseThrow(() -> new RuntimeException("Milestone not found"));

      boolean allDeliverablesCompleted =
          dbm.getDeliverablesIds().stream()
              .allMatch(
                  deliverableId -> {
                    var deliverable =
                        deliverables.stream()
                            .filter(d -> d.getId().equals(deliverableId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Deliverable not found"));
                    return deliverable.getStatus() == Deliverable.Status.ACCEPTED;
                  });

      if (allDeliverablesCompleted) {
        milestone.setStatus(Milestone.Status.COMPLETED);
      }
    }
  }
}
