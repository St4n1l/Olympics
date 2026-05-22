package com.example.Olympics.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BiathlonResultRequestDto(
        @NotNull(message = "Athlete ID is required")
        Long athleteId,

        @NotNull(message = "Ski time is required")
        @DecimalMin(value = "0.001", message = "Ski time must be greater than 0")
        @DecimalMax(value = "99999.999", message = "Ski time seems unrealistically high")
        @Digits(integer = 8, fraction = 3, message = "Ski time must have at most 3 decimal places")
        BigDecimal skiTime,

        @Min(value = 0, message = "Missed shots cannot be negative")
        @Max(value = 20, message = "Missed shots cannot exceed 20")
        int missedShots
) {}
