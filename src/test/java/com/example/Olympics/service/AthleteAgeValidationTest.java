package com.example.Olympics.service;

import com.example.Olympics.entity.Athlete;
import com.example.Olympics.entity.Competition;
import com.example.Olympics.repository.AthleteRepository;
import com.example.Olympics.repository.CompetitionRepository;
import com.example.Olympics.repository.SkiSlalomResultRepository;
import com.example.Olympics.entity.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AthleteAgeValidationTest {

    @Mock
    SkiSlalomResultRepository resultRepo;
    @Mock
    AthleteRepository athleteRepo;
    @Mock
    CompetitionRepository competitionRepo;

    @InjectMocks
    SkiSlalomService service;

    @Test
    void enterRun1_throwsIfAthleteTooYoung() {
        Competition comp = Competition.builder().id(1L)
                .minAge(18).gender(Gender.Male).build();
        Athlete youngAthlete = Athlete.builder().id(1L)
                .name("Young").gender(Gender.Male)
                .dateOfBirth(LocalDate.now().minusYears(16))
                .build();

        when(competitionRepo.findById(1L)).thenReturn(Optional.of(comp));
        when(athleteRepo.findById(1L)).thenReturn(Optional.of(youngAthlete));

        assertThrows(IllegalArgumentException.class, () ->
                service.enterRun1(1L, 1L, new BigDecimal("47.000")));
    }

    @Test
    void enterRun1_throwsIfGenderMismatch() {
        Competition comp = Competition.builder().id(1L)
                .minAge(18).gender(Gender.Male).build();
        Athlete feMaleAthlete = Athlete.builder().id(1L)
                .name("FeMale").gender(Gender.Female)
                .dateOfBirth(LocalDate.now().minusYears(25))
                .build();

        when(competitionRepo.findById(1L)).thenReturn(Optional.of(comp));
        when(athleteRepo.findById(1L)).thenReturn(Optional.of(feMaleAthlete));

        assertThrows(IllegalArgumentException.class, () ->
                service.enterRun1(1L, 1L, new BigDecimal("47.000")));
    }

    @Test
    void enterRun1_succeedsIfAgeAndGenderMatch() {
        Competition comp = Competition.builder().id(1L)
                .minAge(18).gender(Gender.Male).build();
        Athlete athlete = Athlete.builder().id(1L)
                .name("Valid").gender(Gender.Male)
                .dateOfBirth(LocalDate.now().minusYears(25))
                .build();

        when(competitionRepo.findById(1L)).thenReturn(Optional.of(comp));
        when(athleteRepo.findById(1L)).thenReturn(Optional.of(athlete));
        when(resultRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        assertDoesNotThrow(() ->
                service.enterRun1(1L, 1L, new BigDecimal("47.000")));
    }
}