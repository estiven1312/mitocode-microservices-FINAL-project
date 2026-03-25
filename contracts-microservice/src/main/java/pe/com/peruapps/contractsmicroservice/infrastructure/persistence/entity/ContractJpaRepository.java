package pe.com.peruapps.contractsmicroservice.infrastructure.persistence.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContractJpaRepository extends JpaRepository<ContractJpaEntity, UUID> {}
