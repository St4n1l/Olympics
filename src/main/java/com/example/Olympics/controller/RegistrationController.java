package com.example.Olympics.controller;

import com.example.Olympics.dto.RegistrationRequestDto;
import com.example.Olympics.entity.Athlete;
import com.example.Olympics.service.AthleteService;
import com.example.Olympics.service.KeycloakAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller  // not @RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final KeycloakAdminService keycloakAdminService;
    private final AthleteService athleteService;

    @GetMapping("/view")
    public String view(Model model, Authentication authentication) {
        boolean isLoggedIn = authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.isAuthenticated();
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isAdmin", false);
        return "register";
    }

    @PostMapping
    @ResponseBody  // only the POST returns JSON
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequestDto dto) {
        this.keycloakAdminService.createUser(dto.username(), dto.email(), dto.password());
        Athlete athlete = Athlete.builder()
                .name(dto.name())
                .country(dto.country())
                .gender(dto.gender())
                .dateOfBirth(dto.dateOfBirth())
                .build();
        this.athleteService.save(athlete);
        return ResponseEntity.status(HttpStatus.CREATED).body("Athlete registered successfully");
    }
}
