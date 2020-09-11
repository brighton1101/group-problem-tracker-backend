package com.pm.backend.security.representations;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Accessors(chain = true)
public class KeyCloakResource {

    private String name;
    private String type;
    private Set<String> uris;
    private Set<String> scopes;
    private String ownerId;

    public KeyCloakResource() {
        uris = new HashSet<>();
        scopes = new HashSet<>();
        
    }


    public void addScope(String scope) {
        scopes.add(scope);
    }

    public void addUri(String uri) {
        uris.add(uri);
    }

}
