package com.example.Olympics.dto;

import com.example.Olympics.entity.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegistrationRequestDto(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers and underscores")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).+$",
                message = "Password must contain at least one uppercase letter and one number")
        String password,

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotBlank(message = "Country is required")
        @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
        String country,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth
) {}
