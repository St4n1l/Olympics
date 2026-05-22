package com.example.Olympics.service;

import com.example.Olympics.entity.Athlete;
import com.example.Olympics.entity.Competition;
import com.example.Olympics.entity.SkiSlalomResult;
import com.example.Olympics.exception.NotFoundException;
import com.example.Olympics.repository.AthleteRepository;
import com.example.Olympics.repository.CompetitionRepository;
import com.example.Olympics.repository.SkiSlalomResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkiSlalomService {

    private final SkiSlalomResultRepository resultRepo;
    private final AthleteRepository athleteRepo;
    private final CompetitionRepository competitionRepo;

    private static final int RUN2_QUALIFIERS = 30;

    public SkiSlalomResult enterRun1(Long competitionId, Long athleteId, BigDecimal time) {
        Competition comp = competitionRepo.findById(competitionId)
                .orElseThrow(() -> new NotFoundException("Competition not found"));
        Athlete athlete = athleteRepo.findById(athleteId)
                .orElseThrow(() -> new NotFoundException("Athlete not found"));

        int age = Period.between(athlete.getDateOfBirth(), LocalDate.now()).getYears();
        if (age < comp.getMinAge()) {
            throw new IllegalArgumentException(
                    "Athlete is too young. Minimum age is " + comp.getMinAge());
        }
        if (athlete.getGender() != comp.getGender()) {
            throw new IllegalArgumentException(
                    "Athlete gender does not match competition gender");
        }

        SkiSlalomResult result = SkiSlalomResult.builder()
                .competition(comp)
                .athlete(athlete)
                .run1Time(time)
                .finishedRun1(true)
                .build();
        return resultRepo.save(result);
    }

    public List<SkiSlalomResult> getQualifiersForRun2(Long competitionId) {
        return resultRepo.findByCompetitionId(competitionId).stream()
                .filter(SkiSlalomResult::isFinishedRun1)
                .sorted(Comparator.comparing(SkiSlalomResult::getRun1Time))
                .limit(RUN2_QUALIFIERS)
                .collect(Collectors.toList());
    }

    public SkiSlalomResult enterRun2(Long resultId, BigDecimal time) {
        SkiSlalomResult result = resultRepo.findById(resultId).orElseThrow();
        result.setRun2Time(time);
        result.setFinishedRun2(true);
        return resultRepo.save(result);
    }

    // Slowest run1 time goes first in run2 — reversed
    public List<SkiSlalomResult> getRun2StartOrder(Long competitionId) {
        return getQualifiersForRun2(competitionId).stream()
                .sorted(Comparator.comparing(SkiSlalomResult::getRun1Time).reversed())
                .collect(Collectors.toList());
    }

    public List<SkiSlalomResult> getFinalRanking(Long competitionId) {
        return resultRepo.findByCompetitionId(competitionId).stream()
                .filter(r -> r.isFinishedRun1() && r.isFinishedRun2())
                .sorted(Comparator.comparing(SkiSlalomResult::getTotalTime))
                .collect(Collectors.toList());
    }
}