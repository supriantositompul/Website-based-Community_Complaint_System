package com.puas.clientapp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.puas.clientapp.models.dto.request.LoginRequest;
import com.puas.clientapp.models.dto.response.LoginResponse;
import com.puas.clientapp.utils.AuthSessionUtil;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AuthService {

    @Value("${server.base.url}/auth/login")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public Boolean login(LoginRequest loginRequest) {
        try {
            // GET ENDPOINT LOGIN
            ResponseEntity<LoginResponse> res = restTemplate.exchange(baseUrl, HttpMethod.POST,
                    new HttpEntity<>(loginRequest), LoginResponse.class);

            // SET PRINCIPLE
            if (res.getStatusCode() == HttpStatus.OK) {
                setPrinciple(res.getBody(), loginRequest.getPassword());

                // check principle
                Authentication auth = AuthSessionUtil.getAuthentication();

                if (auth != null) {
                    log.info("name: " + auth.getName());
                    log.info("principal: " + auth.getPrincipal());
                    log.info("credentials: " + auth.getCredentials());
                } else {
                    log.error("Authentication is null after login.");
                }

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Login failed: " + e.getMessage());
            return false;
        }
        return false;
    }

    public void setPrinciple(LoginResponse response, String password) {
        List<SimpleGrantedAuthority> authorities = response.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(response.getUsername(),
                password, authorities);

        // set principle
        SecurityContextHolder.getContext().setAuthentication(token);
    }

}
