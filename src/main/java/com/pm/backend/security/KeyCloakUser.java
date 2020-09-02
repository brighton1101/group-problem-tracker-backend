package com.pm.backend.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Setter
@Getter
@Accessors(chain = true)
public class KeyCloakUser {


    private String id;
    private String userName;
    private String firstName;
    private String lastName;

    private String email;
    private String password;

    private Date timeCreated;

    public KeyCloakUser copy() {
        return new KeyCloakUser()
                .setId(this.id)
                .setUserName(this.userName)
                .setFirstName(this.firstName)
                .setLastName(this.lastName)
                .setEmail(this.email)
                .setPassword(this.password);
    }
}
