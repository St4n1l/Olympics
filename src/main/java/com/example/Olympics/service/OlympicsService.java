package com.example.Olympics.service;

import com.example.Olympics.entity.*;
import com.example.Olympics.exception.NotFoundException;
import com.example.Olympics.repository.BiathlonResultRepository;
import com.example.Olympics.repository.CompetitionRepository;
import com.example.Olympics.repository.OlympicsRepository;
import com.example.Olympics.repository.SkiSlalomResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OlympicsService {

    private final OlympicsRepository olympicsRepo;
    private final CompetitionRepository competitionRepo;
    private final SkiSlalomResultRepository slalomRepo;
    private final BiathlonResultRepository biathlonRepo;

    public Olympics save(Olympics olympics) { return olympicsRepo.save(olympics); }
    public Olympics findById(Long id) {
        return olympicsRepo.findById(id).orElseThrow(() -> new NotFoundException("Olympics not found"));
    }
    public List<Olympics> findAll() { return olympicsRepo.findAll(); }

    // Returns top 3 for a slalom competition
    public List<SkiSlalomResult> getSlalomMedalists(Long competitionId) {
        return slalomRepo.findByCompetitionId(competitionId).stream()
                .filter(r -> r.isFinishedRun1() && r.isFinishedRun2())
                .sorted(Comparator.comparing(SkiSlalomResult::getTotalTime))
                .limit(3)
                .collect(Collectors.toList());
    }

    // Returns top 3 for a biathlon competition
    public List<BiathlonResult> getBiathlonMedalists(Long competitionId) {
        return biathlonRepo.findByCompetitionId(competitionId).stream()
                .filter(BiathlonResult::isFinished)
                .sorted(Comparator.comparing(BiathlonResult::getTotalTime))
                .limit(3)
                .collect(Collectors.toList());
    }

    // Medal count per country across all competitions in an olympics
    public Map<String, Long> getMedalCountByCountry(Long olympicsId) {
        Olympics olympics = findById(olympicsId);
        Map<String, Long> medalCount = new HashMap<>();

        for (Competition comp : olympics.getCompetitions()) {
            List<Athlete> medalists = new ArrayList<>();

            if (comp.getType() == CompetitionType.SLALOM) {
                getSlalomMedalists(comp.getId()).stream()
                        .map(SkiSlalomResult::getAthlete)
                        .forEach(medalists::add);
            } else {
                getBiathlonMedalists(comp.getId()).stream()
                        .map(BiathlonResult::getAthlete)
                        .forEach(medalists::add);
            }

            for (Athlete a : medalists) {
                medalCount.merge(a.getCountry(), 1L, Long::sum);
            }
        }
        return medalCount;
    }

    // Average age of all participants across all competitions
    public double getAverageAge(Long olympicsId) {
        Olympics olympics = findById(olympicsId);
        Set<Athlete> allAthletes = new HashSet<>();

        for (Competition comp : olympics.getCompetitions()) {
            if (comp.getType() == CompetitionType.SLALOM) {
                slalomRepo.findByCompetitionId(comp.getId())
                        .stream().map(SkiSlalomResult::getAthlete).forEach(allAthletes::add);
            } else {
                biathlonRepo.findByCompetitionId(comp.getId())
                        .stream().map(BiathlonResult::getAthlete).forEach(allAthletes::add);
            }
        }

        return allAthletes.stream()
                .mapToInt(a -> Period.between(a.getDateOfBirth(), LocalDate.now()).getYears())
                .average()
                .orElse(0);
    }

    // Youngest and oldest medalist across the whole olympics
    public Map<String, Athlete> getYoungestAndOldestMedalist(Long olympicsId) {
        Olympics olympics = findById(olympicsId);
        List<Athlete> allMedalists = new ArrayList<>();

        for (Competition comp : olympics.getCompetitions()) {
            if (comp.getType() == CompetitionType.SLALOM) {
                getSlalomMedalists(comp.getId()).stream()
                        .map(SkiSlalomResult::getAthlete).forEach(allMedalists::add);
            } else {
                getBiathlonMedalists(comp.getId()).stream()
                        .map(BiathlonResult::getAthlete).forEach(allMedalists::add);
            }
        }

        Map<String, Athlete> result = new HashMap<>();
        allMedalists.stream()
                .min(Comparator.comparing(Athlete::getDateOfBirth))
                .ifPresent(a -> result.put("oldest", a));
        allMedalists.stream()
                .max(Comparator.comparing(Athlete::getDateOfBirth))
                .ifPresent(a -> result.put("youngest", a));
        return result;
    }
}