package pe.com.peruapps.authservice.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.peruapps.authservice.domain.model.Role;
import pe.com.peruapps.authservice.domain.model.User;
import pe.com.peruapps.authservice.repository.RoleRepository;
import pe.com.peruapps.authservice.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevelopAdminLoader implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevelopAdminLoader.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminBootstrapProperties adminBootstrapProperties;

    @Override
    public void run(ApplicationArguments args) {
        String adminUsername = adminBootstrapProperties.username();

        if (userRepository.existsByUsername(adminUsername)) {
            LOGGER.info("Admin user '{}' already exists, skipping bootstrap", adminUsername);
            return;
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAll());
        if (roles.isEmpty()) {
            throw new IllegalStateException("Cannot bootstrap admin user: no roles found in database");
        }

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminBootstrapProperties.password()));
        admin.setRoles(roles);

        userRepository.save(admin);
        LOGGER.info("Admin user '{}' created with {} roles", adminUsername, roles.size());
    }
}

