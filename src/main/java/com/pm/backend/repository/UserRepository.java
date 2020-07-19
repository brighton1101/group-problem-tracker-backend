package com.pm.backend.repository;

import com.pm.backend.model.v1.user.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserModel, String> {
}
