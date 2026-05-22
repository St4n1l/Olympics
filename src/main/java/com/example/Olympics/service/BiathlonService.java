package com.example.Olympics.service;

import com.example.Olympics.entity.Athlete;
import com.example.Olympics.entity.BiathlonResult;
import com.example.Olympics.entity.Competition;
import com.example.Olympics.exception.NotFoundException;
import com.example.Olympics.repository.AthleteRepository;
import com.example.Olympics.repository.BiathlonResultRepository;
import com.example.Olympics.repository.CompetitionRepository;
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
public class BiathlonService {

    private final BiathlonResultRepository resultRepo;
    private final AthleteRepository athleteRepo;
    private final CompetitionRepository competitionRepo;

    public BiathlonResult enterResult(Long competitionId, Long athleteId,
                                      BigDecimal skiTime, int missedShots) {
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

        BiathlonResult result = BiathlonResult.builder()
                .competition(comp)
                .athlete(athlete)
                .skiTime(skiTime)
                .missedShots(missedShots)
                .finished(true)
                .build();
        return resultRepo.save(result);
    }

    public List<BiathlonResult> getFinalRanking(Long competitionId) {
        return resultRepo.findByCompetitionId(competitionId).stream()
                .filter(BiathlonResult::isFinished)
                .sorted(Comparator.comparing(BiathlonResult::getTotalTime))
                .collect(Collectors.toList());
    }
}
