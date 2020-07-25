package com.pm.backend.repository;

import com.pm.backend.model.v1.solution.SolutionModel;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SolutionRepository extends MongoRepository<SolutionModel, String> {
    //@Query(value = "{'user._id': ObjectId('?0')}")
    @Query(fields="{group : 0}")
    List<SolutionModel> findByUser_id(ObjectId userId);
    @Query(fields="{group : 0}")
    List<SolutionModel> findByGroup_idOrderByQuestionIdAsc(ObjectId objectId);
}
