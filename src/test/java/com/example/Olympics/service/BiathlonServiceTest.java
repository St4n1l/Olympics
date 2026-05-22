package com.example.Olympics.service;

import com.example.Olympics.entity.Athlete;
import com.example.Olympics.entity.BiathlonResult;
import com.example.Olympics.repository.AthleteRepository;
import com.example.Olympics.repository.BiathlonResultRepository;
import com.example.Olympics.repository.CompetitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BiathlonServiceTest {

    @Mock
    BiathlonResultRepository resultRepo;
    @Mock
    AthleteRepository athleteRepo;
    @Mock
    CompetitionRepository competitionRepo;

    @InjectMocks
    BiathlonService service;

    @Test
    void getFinalRanking_sortsByTotalTime() {
        Athlete a1 = Athlete.builder().id(1L).name("Alice").build();
        Athlete a2 = Athlete.builder().id(2L).name("Bob").build();

        BiathlonResult r1 = BiathlonResult.builder()
                .athlete(a1).skiTime(new BigDecimal("1200.000"))
                .missedShots(2).finished(true).build();

        BiathlonResult r2 = BiathlonResult.builder()
                .athlete(a2).skiTime(new BigDecimal("1150.000"))
                .missedShots(0).finished(true).build();

        when(resultRepo.findByCompetitionId(1L)).thenReturn(List.of(r1, r2));

        List<BiathlonResult> ranking = service.getFinalRanking(1L);

        // Bob: 1150 + 0 penalty = 1150. Alice: 1200 + 120 penalty = 1320
        assertEquals("Bob", ranking.get(0).getAthlete().getName());
        assertEquals("Alice", ranking.get(1).getAthlete().getName());
    }

    @Test
    void getFinalRanking_excludesUnfinished() {
        Athlete a1 = Athlete.builder().id(1L).name("Alice").build();

        BiathlonResult dnf = BiathlonResult.builder()
                .athlete(a1).skiTime(new BigDecimal("1200.000"))
                .missedShots(0).finished(false).build();

        when(resultRepo.findByCompetitionId(1L)).thenReturn(List.of(dnf));

        List<BiathlonResult> ranking = service.getFinalRanking(1L);

        assertTrue(ranking.isEmpty());
    }

    @Test
    void penaltyCalculation_isCorrect() {
        BiathlonResult r = BiathlonResult.builder()
                .skiTime(new BigDecimal("1000.000"))
                .missedShots(3).finished(true).build();

        assertEquals(new BigDecimal("180"), r.getPenaltyTime());
        assertEquals(new BigDecimal("1180.000"), r.getTotalTime());
    }
}
