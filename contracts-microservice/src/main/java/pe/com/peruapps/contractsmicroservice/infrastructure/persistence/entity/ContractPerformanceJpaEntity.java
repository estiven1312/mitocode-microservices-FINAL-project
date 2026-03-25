package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ContractPerformanceJpaEntity {

  private UUID contractId;
}
