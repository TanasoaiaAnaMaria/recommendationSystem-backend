package com.usv.recommendationSystem.repository;

import com.usv.recommendationSystem.entity.Pini;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PiniRepository extends CrudRepository<Pini, UUID> {
}
