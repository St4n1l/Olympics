package com.example.Olympics.controller;

import com.example.Olympics.dto.CompetitionRequestDto;
import com.example.Olympics.entity.Competition;
import com.example.Olympics.entity.CompetitionStatus;
import com.example.Olympics.entity.Olympics;
import com.example.Olympics.exception.NotFoundException;
import com.example.Olympics.repository.CompetitionRepository;
import com.example.Olympics.repository.OlympicsRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/api/competitions")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionRepository competitionRepo;
    private final OlympicsRepository olympicsRepo;

    @GetMapping
    public List<Competition> getAll() { return competitionRepo.findAll(); }

    @PostMapping
    public ResponseEntity<Competition> create(@Valid @RequestBody CompetitionRequestDto dto) {
        Olympics olympics = olympicsRepo.findById(dto.olympicsId())
                .orElseThrow(() -> new NotFoundException("Olympics not found"));
        Competition c = Competition.builder()
                .name(dto.name()).type(dto.type())
                .gender(dto.gender()).minAge(dto.minAge())
                .olympics(olympics).status(CompetitionStatus.OPEN)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(competitionRepo.save(c));
    }
}
