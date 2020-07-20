package com.pm.backend.repository;

import com.mongodb.DBRef;
import com.pm.backend.model.v1.group.GroupModel;
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
    public String findGroupAddUser(String groupId, String userId) {
        Query query = new Query(where("id").is(groupId));
        Update update = new Update();
        update.push("users", new DBRef("users", new ObjectId(userId)));

        GroupModel g = mongoTemplate.findAndModify(query, update, GroupModel.class);
        return "Success";
    }
}
