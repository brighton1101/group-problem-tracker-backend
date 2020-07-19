package com.pm.backend.model.v1.user;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.pm.backend.model.v1.SchemaVersion.v;

@Data
@Document
public class UserModel {
    private String userName;
    private String userPassword;
    private String userId;
    private final int schemaVersion = v;

    //solutions embedded

    //private List<groups> referenced

}
