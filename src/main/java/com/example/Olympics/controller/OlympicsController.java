package com.example.Olympics.controller;

import com.example.Olympics.dto.OlympicsRequestDto;
import com.example.Olympics.entity.Athlete;
import com.example.Olympics.entity.BiathlonResult;
import com.example.Olympics.entity.Olympics;
import com.example.Olympics.entity.SkiSlalomResult;
import com.example.Olympics.service.OlympicsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/olympics")
@RequiredArgsConstructor
public class OlympicsController {
    private final OlympicsService service;

    @GetMapping
    public List<Olympics> getAll() { return service.findAll(); }

    @PostMapping
    public ResponseEntity<Olympics> create(@Valid @RequestBody OlympicsRequestDto dto) {
        Olympics o = Olympics.builder()
                .name(dto.name())
                .year(dto.year())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(o));
    }

    @GetMapping("/{id}/medals")
    public Map<String, Long> getMedalCount(@PathVariable Long id) {
        return service.getMedalCountByCountry(id);
    }

    @GetMapping("/{id}/average-age")
    public Map<String, Double> getAverageAge(@PathVariable Long id) {
        return Map.of("averageAge", service.getAverageAge(id));
    }

    @GetMapping("/{id}/medalist-ages")
    public Map<String, Athlete> getYoungestOldest(@PathVariable Long id) {
        return service.getYoungestAndOldestMedalist(id);
    }

    @GetMapping("/{id}/slalom/{competitionId}/medalists")
    public List<SkiSlalomResult> getSlalomMedalists(@PathVariable Long id,
                                                    @PathVariable Long competitionId) {
        return service.getSlalomMedalists(competitionId);
    }

    @GetMapping("/{id}/biathlon/{competitionId}/medalists")
    public List<BiathlonResult> getBiathlonMedalists(@PathVariable Long id,
                                                     @PathVariable Long competitionId) {
        return service.getBiathlonMedalists(competitionId);
    }
}