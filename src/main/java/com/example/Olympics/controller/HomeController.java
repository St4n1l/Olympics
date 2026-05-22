package com.example.Olympics.controller;

import com.example.Olympics.service.OlympicsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final OlympicsService service;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("olympics", service.findAll());

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            System.out.println("AUTH CLASS: " + authentication.getClass().getName());
            System.out.println("AUTHORITIES: " + authentication.getAuthorities());
            System.out.println("ALL CLAIMS: " + authentication.getPrincipal());
        }

        boolean isAdmin = authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isLoggedIn = authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.isAuthenticated();
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "index";
    }
}
