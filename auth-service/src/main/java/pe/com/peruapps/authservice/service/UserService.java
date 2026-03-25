package pe.com.peruapps.authservice.service;

import pe.com.peruapps.authservice.controller.dto.RegisterUserRequest;
import pe.com.peruapps.authservice.controller.dto.UserResponse;

public interface UserService {

    UserResponse register(RegisterUserRequest request);
}

