package pe.com.peruapps.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.com.peruapps.authservice.controller.dto.RegisterUserRequest;
import pe.com.peruapps.authservice.controller.dto.UserResponse;
import pe.com.peruapps.authservice.domain.exception.exception.RoleNotFoundException;
import pe.com.peruapps.authservice.domain.exception.exception.UserAlreadyExistsException;
import pe.com.peruapps.authservice.domain.model.Role;
import pe.com.peruapps.authservice.domain.model.User;
import pe.com.peruapps.authservice.repository.RoleRepository;
import pe.com.peruapps.authservice.repository.UserRepository;
import pe.com.peruapps.authservice.service.UserService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserResponse register(RegisterUserRequest request) {
    if (userRepository.existsByUsername(request.username())) {
      throw new UserAlreadyExistsException(request.username());
    }

    Set<Role> roles = request.roles().stream().map(this::findRole).collect(Collectors.toSet());

    User usuario = new User();
    usuario.setUsername(request.username());
    usuario.setPassword(passwordEncoder.encode(request.password()));
    usuario.setRoles(roles);

    User saved = userRepository.save(usuario);
    Set<String> roleNames =
        saved.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

    return new UserResponse(saved.getId(), saved.getUsername(), roleNames);
  }

  private Role findRole(String roleName) {
    return roleRepository
        .findByName(roleName)
        .orElseThrow(() -> new RoleNotFoundException(roleName));
  }
}
