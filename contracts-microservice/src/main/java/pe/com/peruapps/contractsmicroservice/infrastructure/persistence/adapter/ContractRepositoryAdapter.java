package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.adapter;
import pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pe.com.peruapps.contractsmicroservice.application.port.ContractRepository;
import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractApproval;
import pe.com.peruapps.contractsmicroservice.domain.exception.ContractNotFound;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContractRepositoryAdapter implements ContractRepository {

  private final ContractJpaRepository jpaRepository;
  private final ContractApprovalJpaRepository approvalJpaRepository;
  private final ContractJpaMapper mapper;

  @Override
  public Contract save(Contract contract) {
    return mapper.toDomain(jpaRepository.save(mapper.toEntity(contract)));
  }

  @Override
  public ContractApproval saveApproval(Contract.ContractId contractId, ContractApproval approval) {
    ContractJpaEntity contractEntity =
        jpaRepository
            .findById(contractId.value())
            .orElseThrow(
                () ->
                    new ContractNotFound(
                        contractId, "Contract not found with id: " + contractId.value()));

    ContractApprovalJpaEntity savedApproval =
        approvalJpaRepository.save(mapper.toApprovalEntity(approval, contractEntity));
    return mapper.toApprovalDomain(savedApproval);
  }

  @Override
  public Optional<Contract> findById(Contract.ContractId id) {
    return jpaRepository.findById(id.value()).map(mapper::toDomain);
  }
}
