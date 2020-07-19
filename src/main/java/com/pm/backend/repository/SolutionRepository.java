package com.pm.backend.repository;

import com.pm.backend.model.v1.solution.SolutionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SolutionRepository extends MongoRepository<SolutionModel, String> {
}
