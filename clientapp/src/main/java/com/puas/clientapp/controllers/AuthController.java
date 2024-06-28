package com.puas.clientapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.puas.clientapp.models.dto.request.LoginRequest;
import com.puas.clientapp.services.AuthService;
import com.puas.clientapp.utils.AuthSessionUtil;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/login")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @GetMapping
    public String loginView(LoginRequest loginRequest) {
        Authentication auth = AuthSessionUtil.getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken) {
            return "auth/login";
        }
        return "redirect:/home";
    }

    @PostMapping
    public String login(LoginRequest loginRequest) {
        if (!authService.login(loginRequest)) {
            return "redirect:/login?error=true";
        }
        return "redirect:/home";
    }
}
