package com.puas.serverapp.controllers;

import com.puas.serverapp.models.dto.request.LoginRequest;
import com.puas.serverapp.models.dto.request.RegistrationRequest;
import com.puas.serverapp.models.dto.response.LoginResponse;
import com.puas.serverapp.models.entities.User;

public interface AuthController {
    User registration(RegistrationRequest registrationRequest);
    
    LoginResponse login(LoginRequest loginRequest);

}
