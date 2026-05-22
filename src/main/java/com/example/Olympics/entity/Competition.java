package com.example.Olympics.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competition {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private CompetitionType type; // SLALOM, BIATHLON

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private int minAge;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "olympics_id")
    private Olympics olympics;

    @Enumerated(EnumType.STRING)
    private CompetitionStatus status; // OPEN, CLOSED
}
