package com.pm.backend.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Date;
import java.util.List;

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

    private List<String> roles;

    public KeyCloakUser() {

    }

    public KeyCloakUser(UserRepresentation userRepresentation) {
        this.setId(userRepresentation.getId())
                .setUserName(userRepresentation.getUsername())
                .setFirstName(userRepresentation.getFirstName())
                .setLastName(userRepresentation.getLastName())
                .setEmail(userRepresentation.getEmail())
                .setTimeCreated(new Date(userRepresentation.getCreatedTimestamp()));
    }

    public KeyCloakUser copy() {
        return new KeyCloakUser()
                .setId(this.id)
                .setUserName(this.userName)
                .setFirstName(this.firstName)
                .setLastName(this.lastName)
                .setEmail(this.email)
                .setPassword(this.password)
                .setTimeCreated(new Date(this.timeCreated.getTime()));
    }

    @Override
    public String toString() {
        return "KeyCloakUser{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", timeCreated=" + timeCreated +
                ", roles=" + roles +
                '}';
    }
}
