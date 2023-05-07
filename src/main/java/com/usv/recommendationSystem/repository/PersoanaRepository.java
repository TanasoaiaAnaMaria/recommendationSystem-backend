package com.usv.recommendationSystem.repository;

import com.usv.recommendationSystem.entity.Persoana;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersoanaRepository extends CrudRepository<Persoana, UUID> {
    Optional<Persoana> findByEmail(String email);
}
