package com.example.Olympics.repository;

import com.example.Olympics.entity.Olympics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OlympicsRepository extends JpaRepository<Olympics, Long> {}