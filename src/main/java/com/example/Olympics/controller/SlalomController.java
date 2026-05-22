package com.example.Olympics.controller;

import com.example.Olympics.dto.SlalomResultRequestDto;
import com.example.Olympics.entity.SkiSlalomResult;
import com.example.Olympics.repository.CompetitionRepository;
import com.example.Olympics.service.SkiSlalomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/api/slalom")
@RequiredArgsConstructor
public class SlalomController {

    private final CompetitionRepository competitionRepo;
    private final SkiSlalomService service;

    @PostMapping("/{competitionId}/run1")
    public SkiSlalomResult enterRun1(@PathVariable Long competitionId,
                                     @Valid @RequestBody SlalomResultRequestDto dto) {
        return service.enterRun1(competitionId, dto.athleteId(), dto.time());
    }

    @GetMapping("/{competitionId}/qualifiers")
    public List<SkiSlalomResult> getQualifiers(@PathVariable Long competitionId) {
        return service.getQualifiersForRun2(competitionId);
    }

    @GetMapping("/{competitionId}/run2-order")
    public List<SkiSlalomResult> getRun2Order(@PathVariable Long competitionId) {
        return service.getRun2StartOrder(competitionId);
    }

    @PutMapping("/results/{resultId}/run2")
    public SkiSlalomResult enterRun2(@PathVariable Long resultId,
                                     @RequestBody Map<String, BigDecimal> body) {
        return service.enterRun2(resultId, body.get("time"));
    }

    @GetMapping("/{competitionId}/ranking")
    public List<SkiSlalomResult> getRanking(@PathVariable Long competitionId) {
        return service.getFinalRanking(competitionId);
    }
}