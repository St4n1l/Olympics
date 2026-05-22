package com.example.Olympics.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkiSlalomResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Athlete athlete;

    @ManyToOne
    private Competition competition;

    @Column(precision = 10, scale = 3)
    private BigDecimal run1Time;

    @Column(precision = 10, scale = 3)
    private BigDecimal run2Time;

    private boolean finishedRun1 = false;
    private boolean finishedRun2 = false;

    public BigDecimal getTotalTime() {
        if (!finishedRun1 || !finishedRun2) return null;
        return run1Time.add(run2Time);
    }
}