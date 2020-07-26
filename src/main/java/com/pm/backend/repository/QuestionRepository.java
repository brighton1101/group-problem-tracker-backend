package com.pm.backend.repository;

import com.pm.backend.model.v1.question.QuestionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<QuestionModel, Integer> {
}
