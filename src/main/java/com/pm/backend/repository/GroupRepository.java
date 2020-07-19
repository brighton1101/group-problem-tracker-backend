package com.pm.backend.repository;

import com.pm.backend.model.v1.group.GroupModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<GroupModel, String> {
   // List<GroupModel> findByUser(GroupModel group);
}
