package com.puas.serverapp.services;

import com.puas.serverapp.models.dto.request.LoginRequest;
import com.puas.serverapp.models.dto.request.RegistrationRequest;
import com.puas.serverapp.models.dto.response.LoginResponse;
import com.puas.serverapp.models.entities.User;

public interface AuthService {
    User registration(RegistrationRequest registrationRequest);

    LoginResponse login(LoginRequest loginRequest);
}
