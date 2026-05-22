package com.example.Olympics.controller;

import com.example.Olympics.service.OlympicsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/olympics")
@RequiredArgsConstructor
public class OlympicsViewController {

    private final OlympicsService service;

    @GetMapping("/view")
    public String view(Model model, Authentication authentication) {
        model.addAttribute("olympics", service.findAll());
        addAuthAttributes(model, authentication);
        return "index";
    }

    @GetMapping("/{id}/view")
    public String detail(@PathVariable Long id, Model model, Authentication authentication) {
        model.addAttribute("olympics", service.findById(id));
        model.addAttribute("medals", service.getMedalCountByCountry(id));
        model.addAttribute("averageAge", service.getAverageAge(id));
        model.addAttribute("medalistAges", service.getYoungestAndOldestMedalist(id));
        addAuthAttributes(model, authentication);
        return "olympics-detail";
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

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("olympics", service.findAll());
        addAuthAttributes(model, authentication);
        return "index";
    }
}