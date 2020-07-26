package com.pm.backend.repository;

import com.mongodb.DBRef;
import com.pm.backend.controller.v1.request.UserJoinGroupRequest;
import com.pm.backend.model.v1.group.GroupModel;
import com.pm.backend.model.v1.user.UserModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class CustomGroupRepositoryImpl implements CustomGroupRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public String findGroupAddUser(UserJoinGroupRequest request) {
        Query query = new Query(where("id").is(request.getGroupId()));
        Update update = new Update();

        //TODO move this logic to a mapper package
        UserModel user = new UserModel();
        user.setId(request.getUserId());
        user.setUserName(request.getUserName());
        update.push("users", user);
        GroupModel g = mongoTemplate.findAndModify(query, update, GroupModel.class);
        return "Success";
    }
}
