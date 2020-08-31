package com.pm.backend.security;

import lombok.Getter;
import org.springframework.core.env.Environment;

@Getter
public class KeyCloakRealm {
    private final String serverUrl;
    private final String realmUrl;
    private final String realmName;
    private final String clientId;
    private final String clientSecret;

    public KeyCloakRealm(String serverUrl, String realmUrl, String realmName, String clientId, String clientSecret) {
        this.serverUrl = serverUrl;
        this.realmUrl = realmUrl;
        this.realmName = realmName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public static KeyCloakRealm userRealm(Environment env) {
        return new KeyCloakRealm(
                env.getProperty("keycloak.server"),
                env.getProperty("keycloak.user-realm.url"),
                env.getProperty("keycloak.user-realm.name"),
                env.getProperty("keycloak.user-realm.client.id"),
                env.getProperty("keycloak.user-realm.client.secret")
        );
    }

}
