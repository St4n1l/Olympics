package com.example.Olympics.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SlalomResultRequestDto(
        @NotNull(message = "Athlete ID is required")
        Long athleteId,

        @NotNull(message = "Time is required")
        @DecimalMin(value = "0.001", message = "Time must be greater than 0")
        @DecimalMax(value = "999.999", message = "Time seems unrealistically high")
        @Digits(integer = 6, fraction = 3, message = "Time must have at most 3 decimal places")
        BigDecimal time
) {}