package com.pm.backend.model.v1.group;


import com.pm.backend.model.v1.user.UserModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;


@Data
@Document(collection = "groups")
public class GroupModel {
    @ApiModelProperty(hidden = true)
    private String id;
    private String groupName;

    //private HashMap<String, List<String>> questionUserMap;
    //private HashMap<String, Integer>userSolutionCountMap;

    private UserModel groupOwner;

    //List<users> referenced
    @DBRef(lazy = true)
    private List<UserModel> users;

    @ApiModelProperty(hidden = true)
    private int schemaVersion;

}
