package com.puas.serverapp.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.puas.serverapp.models.dto.request.LoginRequest;
import com.puas.serverapp.models.dto.request.RegistrationRequest;
import com.puas.serverapp.models.dto.response.LoginResponse;
import com.puas.serverapp.models.entities.Role;
import com.puas.serverapp.models.entities.User;
import com.puas.serverapp.repositories.UserRepository;
import com.puas.serverapp.services.AuthService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private RoleServiceImpl roleServiceImpl;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private AppUserDetailService appUserDetailService;

    @Override
    public User registration(RegistrationRequest registrationRequest) {
        User user = modelMapper.map(registrationRequest, User.class);

        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        List<Role> roles = Collections.singletonList(roleServiceImpl.getById(1));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User addRole(Integer userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        List<Role> roles = user.getRoles();
        roles.add(roleServiceImpl.getById(role.getId()));

        user.setRoles(roles);
        return userRepository.save(user);

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());

        Authentication auth = authenticationManager.authenticate(authRequest);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        UserDetails userDetails = appUserDetailService.loadUserByUsername(loginRequest.getUsername());

        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsername(), loginRequest.getPassword()).get();

        List<String> authorities = userDetails.getAuthorities().stream().map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(userDetails.getUsername());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setAuthorities(authorities);

        return loginResponse;
    }

}
