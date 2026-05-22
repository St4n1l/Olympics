package com.example.Olympics.dto;

import com.example.Olympics.entity.Gender;
import java.time.LocalDate;

public record AthleteDto(
        Long id,
        String name,
        String country,
        Gender gender,
        LocalDate dateOfBirth
) {}
