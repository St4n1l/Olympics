package com.example.Olympics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Athlete {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        private String name;

        @NotBlank
        private String country;

        @Enumerated(EnumType.STRING)
        private Gender gender;

        @Past
        private LocalDate dateOfBirth;
}
