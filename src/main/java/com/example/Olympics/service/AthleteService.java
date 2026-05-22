package com.example.Olympics.service;

import com.example.Olympics.entity.Athlete;
import com.example.Olympics.exception.NotFoundException;
import com.example.Olympics.repository.AthleteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AthleteService {
    private final AthleteRepository repo;

    public Athlete save(Athlete athlete) { return repo.save(athlete); }
    public Athlete findById(Long id) { return repo.findById(id).orElseThrow(() -> new NotFoundException("Athlete not found")); }
    public List<Athlete> findAll() { return repo.findAll(); }
    public void delete(Long id) { repo.deleteById(id); }
    public Athlete update(Long id, Athlete updated) {
        Athlete a = findById(id);
        a.setName(updated.getName());
        a.setCountry(updated.getCountry());
        a.setGender(updated.getGender());
        a.setDateOfBirth(updated.getDateOfBirth());
        return repo.save(a);
    }
}
