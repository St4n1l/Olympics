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
public class BiathlonResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Athlete athlete;

    @ManyToOne
    private Competition competition;

    @Column(precision = 10, scale = 3)
    private BigDecimal skiTime;

    private int missedShots;

    private boolean finished = false;

    // 1 minute penalty per missed shot
    public BigDecimal getPenaltyTime() {
        return BigDecimal.valueOf(missedShots * 60L);
    }

    public BigDecimal getTotalTime() {
        if (!finished) return null;
        return skiTime.add(getPenaltyTime());
    }
}