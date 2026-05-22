package com.example.Olympics.service;

import com.example.Olympics.entity.*;
import com.example.Olympics.repository.BiathlonResultRepository;
import com.example.Olympics.repository.CompetitionRepository;
import com.example.Olympics.repository.OlympicsRepository;
import com.example.Olympics.repository.SkiSlalomResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OlympicsServiceTest {

    @Mock
    OlympicsRepository olympicsRepo;
    @Mock
    CompetitionRepository competitionRepo;
    @Mock
    SkiSlalomResultRepository slalomRepo;
    @Mock
    BiathlonResultRepository biathlonRepo;

    @InjectMocks
    OlympicsService service;

    @Test
    void getSlalomMedalists_returnsTop3() {
        Athlete a1 = Athlete.builder().id(1L).name("Alice").country("BG").build();
        Athlete a2 = Athlete.builder().id(2L).name("Bob").country("NO").build();
        Athlete a3 = Athlete.builder().id(3L).name("Carl").country("AT").build();
        Athlete a4 = Athlete.builder().id(4L).name("Dave").country("DE").build();

        List<SkiSlalomResult> results = List.of(
                SkiSlalomResult.builder().athlete(a1)
                        .run1Time(new BigDecimal("47.000")).run2Time(new BigDecimal("48.000"))
                        .finishedRun1(true).finishedRun2(true).build(),
                SkiSlalomResult.builder().athlete(a2)
                        .run1Time(new BigDecimal("46.000")).run2Time(new BigDecimal("47.000"))
                        .finishedRun1(true).finishedRun2(true).build(),
                SkiSlalomResult.builder().athlete(a3)
                        .run1Time(new BigDecimal("49.000")).run2Time(new BigDecimal("50.000"))
                        .finishedRun1(true).finishedRun2(true).build(),
                SkiSlalomResult.builder().athlete(a4)
                        .run1Time(new BigDecimal("50.000")).run2Time(new BigDecimal("51.000"))
                        .finishedRun1(true).finishedRun2(true).build()
        );

        when(slalomRepo.findByCompetitionId(1L)).thenReturn(results);

        List<SkiSlalomResult> medalists = service.getSlalomMedalists(1L);

        assertEquals(3, medalists.size());
        assertEquals("Bob", medalists.get(0).getAthlete().getName());
        assertEquals("Alice", medalists.get(1).getAthlete().getName());
        assertEquals("Carl", medalists.get(2).getAthlete().getName());
    }

    @Test
    void getMedalCountByCountry_countsCorrectly() {
        Olympics olympics = Olympics.builder().id(1L).name("Test").year(2026)
                .competitions(new ArrayList<>()).build();

        Athlete a1 = Athlete.builder().id(1L).name("Alice").country("Bulgaria").build();
        Athlete a2 = Athlete.builder().id(2L).name("Bob").country("Norway").build();
        Athlete a3 = Athlete.builder().id(3L).name("Carl").country("Bulgaria").build();

        Competition slalom = Competition.builder().id(1L)
                .type(CompetitionType.SLALOM).build();
        olympics.getCompetitions().add(slalom);

        when(olympicsRepo.findById(1L)).thenReturn(Optional.of(olympics));
        when(slalomRepo.findByCompetitionId(1L)).thenReturn(List.of(
                SkiSlalomResult.builder().athlete(a1)
                        .run1Time(new BigDecimal("47.000")).run2Time(new BigDecimal("48.000"))
                        .finishedRun1(true).finishedRun2(true).build(),
                SkiSlalomResult.builder().athlete(a2)
                        .run1Time(new BigDecimal("46.000")).run2Time(new BigDecimal("47.000"))
                        .finishedRun1(true).finishedRun2(true).build(),
                SkiSlalomResult.builder().athlete(a3)
                        .run1Time(new BigDecimal("49.000")).run2Time(new BigDecimal("50.000"))
                        .finishedRun1(true).finishedRun2(true).build()
        ));

        Map<String, Long> medals = service.getMedalCountByCountry(1L);

        assertEquals(2L, medals.get("Bulgaria"));
        assertEquals(1L, medals.get("Norway"));
    }
}
