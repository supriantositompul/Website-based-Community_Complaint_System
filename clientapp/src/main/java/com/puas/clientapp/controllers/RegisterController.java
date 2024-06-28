package com.puas.clientapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.puas.clientapp.models.dto.request.RegisterRequest;
import com.puas.clientapp.services.RegisterService;
import com.puas.clientapp.utils.AuthSessionUtil;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @GetMapping
    public String registerView(RegisterRequest registerRequest, Model model) {
        Authentication auth = AuthSessionUtil.getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken) {
            return "auth/register";
        }

        return "redirect:/";
    }

    @PostMapping
    public String register(@ModelAttribute RegisterRequest registerRequest, Model model) {
        boolean isRegistered = registerService.register(registerRequest);

        if (isRegistered) {
            return "redirect:/auth/login"; // Redirect to login page after successful registration
        } else {
            model.addAttribute("error", "Failed to register. Please try again.");
            return "auth/register";
        }
    }
}