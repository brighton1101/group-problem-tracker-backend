package com.pm.backend.model.v1.user;


import com.pm.backend.model.v1.group.GroupModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Document(collection = "users")
public class UserModel {

    @ApiModelProperty(hidden = true)
    private String id;
    private String userName;
    private String userPassword;

    @ApiModelProperty(hidden = true)
    private int schemaVersion;

    //solutions embedded?

    //private List<groups> referenced
    @DBRef(lazy=true)
    private List<GroupModel> groups;


}
