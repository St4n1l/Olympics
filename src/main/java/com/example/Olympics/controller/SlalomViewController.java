package com.example.Olympics.controller;

import com.example.Olympics.repository.CompetitionRepository;
import com.example.Olympics.service.SkiSlalomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/slalom")
@RequiredArgsConstructor
public class SlalomViewController {

    private final SkiSlalomService service;
    private final CompetitionRepository competitionRepo;

    @GetMapping("/{competitionId}/ranking/view")
    public String rankingView(@PathVariable Long competitionId, Model model,
                              Authentication authentication) {
        model.addAttribute("competition",
                competitionRepo.findById(competitionId).orElseThrow());
        model.addAttribute("results", service.getFinalRanking(competitionId));
        addAuthAttributes(model, authentication);
        return "ranking-slalom";
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

