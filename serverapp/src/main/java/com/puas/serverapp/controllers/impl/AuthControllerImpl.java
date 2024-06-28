package com.puas.serverapp.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puas.serverapp.controllers.AuthController;
import com.puas.serverapp.models.dto.request.LoginRequest;
import com.puas.serverapp.models.dto.request.RegistrationRequest;
import com.puas.serverapp.models.dto.response.LoginResponse;
import com.puas.serverapp.models.entities.Role;
import com.puas.serverapp.models.entities.User;
import com.puas.serverapp.services.impl.AuthServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

    @Autowired
    private AuthServiceImpl authServiceImpl;

    @Override
    @PostMapping("/registration")
    public User registration(@RequestBody RegistrationRequest registrationRequest) {
        return authServiceImpl.registration(registrationRequest);
    }

    @PutMapping("/add-role/{idUser}")
    public User addRoles(@PathVariable Integer idUser, @RequestBody Role role) {
        return authServiceImpl.addRole(idUser, role);
    }

    @Override
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authServiceImpl.login(loginRequest);
    }

}
