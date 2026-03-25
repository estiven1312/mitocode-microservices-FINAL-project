package pe.com.peruapps.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.peruapps.authservice.domain.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
