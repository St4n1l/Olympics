package com.example.Olympics.controller;

import com.example.Olympics.dto.AthleteRequestDto;
import com.example.Olympics.entity.Athlete;
import com.example.Olympics.service.AthleteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/athletes")
@RequiredArgsConstructor
public class AthleteController {
    private final AthleteService service;

    @GetMapping
    public List<Athlete> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Athlete getById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Athlete> create(@Valid @RequestBody AthleteRequestDto dto) {
        Athlete a = Athlete.builder()
                .name(dto.name()).country(dto.country())
                .gender(dto.gender()).dateOfBirth(dto.dateOfBirth())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(a));
    }

    @PutMapping("/{id}")
    public Athlete update(@PathVariable Long id, @Valid @RequestBody AthleteRequestDto dto) {
        Athlete a = Athlete.builder()
                .name(dto.name()).country(dto.country())
                .gender(dto.gender()).dateOfBirth(dto.dateOfBirth())
                .build();
        return service.update(id, a);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}