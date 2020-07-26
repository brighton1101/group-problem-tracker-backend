package com.pm.backend.controller.v1.request;

import com.pm.backend.model.v1.user.UserModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class UserCreateGroupRequest {
    @ApiModelProperty(hidden = true)
    private String id;
    private String groupName;
    private UserModel groupOwner;
    private List<UserModel> users;

    @ApiModelProperty(hidden = true)
    private int schemaVersion;
}
