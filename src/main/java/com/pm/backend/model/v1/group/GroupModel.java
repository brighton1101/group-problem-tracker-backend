package com.pm.backend.model.v1.group;


import com.pm.backend.model.v1.user.UserModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;

import static com.pm.backend.model.v1.SchemaVersion.v;


@Data
@Document
public class GroupModel {
    private String groupName;

    private HashMap<String, List<String>> questionUserMap;
    private HashMap<String, Integer>userSolutionCountMap;


    //List<users> referenced
    @DBRef
    private List<UserModel> users;
    private final int schemaVersion = v;

}
