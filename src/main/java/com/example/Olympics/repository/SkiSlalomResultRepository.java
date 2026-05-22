package com.example.Olympics.repository;

import com.example.Olympics.entity.SkiSlalomResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkiSlalomResultRepository extends JpaRepository<SkiSlalomResult, Long> {
    List<SkiSlalomResult> findByCompetitionId(Long competitionId);
}
