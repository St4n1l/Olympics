package com.example.Olympics.controller;

import com.example.Olympics.dto.BiathlonResultRequestDto;
import com.example.Olympics.entity.BiathlonResult;
import com.example.Olympics.repository.CompetitionRepository;
import com.example.Olympics.service.BiathlonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/api/biathlon")
@RequiredArgsConstructor
public class BiathlonController {

    private final CompetitionRepository competitionRepo;
    private final BiathlonService service;

    @PostMapping("/{competitionId}/result")
    public BiathlonResult enterResult(@PathVariable Long competitionId,
                                      @Valid @RequestBody BiathlonResultRequestDto dto) {
        return service.enterResult(competitionId, dto.athleteId(),
                dto.skiTime(), dto.missedShots());
    }

    @GetMapping("/{competitionId}/ranking")
    public List<BiathlonResult> getRanking(@PathVariable Long competitionId) {
        return service.getFinalRanking(competitionId);
    }
}
