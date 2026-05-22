package com.example.Olympics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        addAuthAttributes(model, authentication);
        return "admin/dashboard";
    }

    @GetMapping("/olympics/create")
    public String createOlympics(Model model, Authentication authentication) {
        addAuthAttributes(model, authentication);
        return "admin/create-olympics";
    }

    @GetMapping("/competitions/create")
    public String createCompetition(Model model, Authentication authentication) {
        addAuthAttributes(model, authentication);
        return "admin/create-competition";
    }

    @GetMapping("/slalom/results")
    public String slalomResults(Model model, Authentication authentication) {
        addAuthAttributes(model, authentication);
        return "admin/slalom-results";
    }

    @GetMapping("/biathlon/results")
    public String biathlonResults(Model model, Authentication authentication) {
        addAuthAttributes(model, authentication);
        return "admin/biathlon-results";
    }

    private void addAuthAttributes(Model model, Authentication authentication) {
        boolean isAdmin = authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isLoggedIn = authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.isAuthenticated();
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isLoggedIn", isLoggedIn);
    }
}