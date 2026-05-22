package com.example.Olympics.service;

import com.example.Olympics.entity.Athlete;
import com.example.Olympics.entity.Competition;
import com.example.Olympics.entity.SkiSlalomResult;
import com.example.Olympics.repository.AthleteRepository;
import com.example.Olympics.repository.CompetitionRepository;
import com.example.Olympics.repository.SkiSlalomResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkiSlalomServiceTest {

    @Mock
    SkiSlalomResultRepository resultRepo;
    @Mock
    AthleteRepository athleteRepo;
    @Mock
    CompetitionRepository competitionRepo;

    @InjectMocks
    SkiSlalomService service;

    @Test
    void getFinalRanking_sortsbyTotalTime() {
        Competition comp = Competition.builder().id(1L).build();

        Athlete a1 = Athlete.builder().id(1L).name("Alice").build();
        Athlete a2 = Athlete.builder().id(2L).name("Bob").build();

        SkiSlalomResult r1 = SkiSlalomResult.builder()
                .athlete(a1).competition(comp)
                .run1Time(new BigDecimal("50.000"))
                .run2Time(new BigDecimal("48.000"))
                .finishedRun1(true).finishedRun2(true).build();

        SkiSlalomResult r2 = SkiSlalomResult.builder()
                .athlete(a2).competition(comp)
                .run1Time(new BigDecimal("47.000"))
                .run2Time(new BigDecimal("46.000"))
                .finishedRun1(true).finishedRun2(true).build();

        when(resultRepo.findByCompetitionId(1L)).thenReturn(List.of(r1, r2));

        List<SkiSlalomResult> ranking = service.getFinalRanking(1L);

        assertEquals("Bob", ranking.get(0).getAthlete().getName());
        assertEquals("Alice", ranking.get(1).getAthlete().getName());
    }

    @Test
    void getFinalRanking_excludesDNF() {
        Competition comp = Competition.builder().id(1L).build();
        Athlete a1 = Athlete.builder().id(1L).name("Alice").build();

        SkiSlalomResult dnf = SkiSlalomResult.builder()
                .athlete(a1).competition(comp)
                .run1Time(new BigDecimal("50.000"))
                .finishedRun1(true).finishedRun2(false).build();

        when(resultRepo.findByCompetitionId(1L)).thenReturn(List.of(dnf));

        List<SkiSlalomResult> ranking = service.getFinalRanking(1L);

        assertTrue(ranking.isEmpty());
    }

    @Test
    void getQualifiersForRun2_returnsTop30() {
        Competition comp = Competition.builder().id(1L).build();

        List<SkiSlalomResult> results = new ArrayList<>();
        for (int i = 1; i <= 35; i++) {
            results.add(SkiSlalomResult.builder()
                    .athlete(Athlete.builder().id((long) i).name("Athlete " + i).build())
                    .competition(comp)
                    .run1Time(new BigDecimal(i + ".000"))
                    .finishedRun1(true).finishedRun2(false).build());
        }

        when(resultRepo.findByCompetitionId(1L)).thenReturn(results);

        List<SkiSlalomResult> qualifiers = service.getQualifiersForRun2(1L);

        assertEquals(30, qualifiers.size());
    }
}
