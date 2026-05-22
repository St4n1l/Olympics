package com.example.Olympics.dto;

import com.example.Olympics.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AthleteRequestDto(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Country is required")
        @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
        String country,

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth
) {}
