package com.pm.backend.model.v1.user;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Accessors(chain = true)
@Document(collection = "users")
public class UserModel {

    @Id
    private String id;
    private String userName;
    private String userPassword;

    @ApiModelProperty(hidden = true)
    private int schemaVersion;


}
