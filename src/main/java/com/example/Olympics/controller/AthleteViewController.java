package com.example.Olympics.controller;

import com.example.Olympics.service.AthleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/athletes")
@RequiredArgsConstructor
public class AthleteViewController {

    private final AthleteService service;

    @GetMapping("/view")
    public String view(Model model, Authentication authentication) {
        model.addAttribute("athletes", service.findAll());
        addAuthAttributes(model, authentication);
        return "athletes";
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