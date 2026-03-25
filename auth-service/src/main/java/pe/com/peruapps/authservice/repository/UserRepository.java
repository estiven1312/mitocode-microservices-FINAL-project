package pe.com.peruapps.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.peruapps.authservice.domain.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}

