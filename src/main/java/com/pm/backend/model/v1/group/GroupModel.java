package com.pm.backend.model.v1.group;


import com.pm.backend.model.v1.user.UserModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Document(collection = "groups")
@Accessors(chain = true)
public class GroupModel {
    @Id
    private String id;
    private String groupName;
    private UserModel groupOwner;
    private List<UserModel> users;

    @ApiModelProperty(hidden = true)
    private int schemaVersion;

}
