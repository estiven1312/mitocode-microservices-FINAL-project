package pe.com.peruapps.contractsmicroservice.application.port;

import pe.com.peruapps.contractsmicroservice.domain.entity.Contract;
import pe.com.peruapps.contractsmicroservice.domain.entity.ContractApproval;

import java.util.Optional;

public interface ContractRepository {
  Contract save(Contract contract);
  ContractApproval saveApproval(Contract.ContractId contractId, ContractApproval approval);
  Optional<Contract> findById(Contract.ContractId id);
}
