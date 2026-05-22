package com.example.Olympics.dto;

import com.example.Olympics.entity.Gender;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void athleteDto_validInput_noViolations() {
        AthleteRequestDto dto = new AthleteRequestDto(
                "John Doe", "Bulgaria", Gender.Male,
                LocalDate.of(2000, 1, 15));
        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void athleteDto_blankName_hasViolation() {
        AthleteRequestDto dto = new AthleteRequestDto(
                "", "Bulgaria", Gender.Male,
                LocalDate.of(2000, 1, 15));
        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void athleteDto_futureDob_hasViolation() {
        AthleteRequestDto dto = new AthleteRequestDto(
                "John", "Bulgaria", Gender.Male,
                LocalDate.now().plusDays(1));
        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void slalomDto_negativeTime_hasViolation() {
        SlalomResultRequestDto dto = new SlalomResultRequestDto(
                1L, new BigDecimal("-1.000"));
        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void biathlonDto_negativeMissedShots_hasViolation() {
        BiathlonResultRequestDto dto = new BiathlonResultRequestDto(
                1L, new BigDecimal("1000.000"), -1);
        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void registrationDto_weakPassword_hasViolation() {
        RegistrationRequestDto dto = new RegistrationRequestDto(
                "johndoe", "john@test.com", "weakpass",
                "John Doe", Gender.Male, "Bulgaria",
                LocalDate.of(2000, 1, 15));
        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void registrationDto_invalidEmail_hasViolation() {
        RegistrationRequestDto dto = new RegistrationRequestDto(
                "johndoe", "notanemail", "Strong1pass",
                "John Doe", Gender.Male, "Bulgaria",
                LocalDate.of(2000, 1, 15));
        assertFalse(validator.validate(dto).isEmpty());
    }
}