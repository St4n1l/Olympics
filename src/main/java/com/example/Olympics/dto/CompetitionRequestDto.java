package com.example.Olympics.dto;

import com.example.Olympics.entity.CompetitionType;
import com.example.Olympics.entity.Gender;
import jakarta.validation.constraints.*;

public record CompetitionRequestDto(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotNull(message = "Competition type is required")
        CompetitionType type,

        @NotNull(message = "Gender is required")
        Gender gender,

        @Min(value = 14, message = "Minimum age must be at least 14")
        @Max(value = 80, message = "Minimum age cannot exceed 80")
        int minAge,

        @NotNull(message = "Olympics ID is required")
        Long olympicsId
) {}