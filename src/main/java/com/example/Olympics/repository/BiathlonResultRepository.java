package com.example.Olympics.repository;

import com.example.Olympics.entity.BiathlonResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiathlonResultRepository extends JpaRepository<BiathlonResult, Long> {
    List<BiathlonResult> findByCompetitionId(Long competitionId);
}
