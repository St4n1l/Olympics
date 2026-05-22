package com.example.Olympics.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OlympicsRequestDto(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @Min(value = 1900, message = "Year must be after 1900")
        @Max(value = 2100, message = "Year must be before 2100")
        int year
) {}
