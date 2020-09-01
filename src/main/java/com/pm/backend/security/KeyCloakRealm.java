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

    //for admin realm
    private final String adminName;
    private final String adminPassword;

    public KeyCloakRealm(String serverUrl, String realmUrl, String realmName, String clientId, String clientSecret, String adminName, String adminPassword) {
        this.serverUrl = serverUrl;
        this.realmUrl = realmUrl;
        this.realmName = realmName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.adminName = adminName;
        this.adminPassword = adminPassword;
    }

    public static KeyCloakRealm userRealm(Environment env) {
        return new KeyCloakRealm(
                env.getProperty("keycloak.server"),
                env.getProperty("keycloak.user-realm.url"),
                env.getProperty("keycloak.user-realm.name"),
                env.getProperty("keycloak.user-realm.client.id"),
                env.getProperty("keycloak.user-realm.client.secret"),
                null,
                null);
    }

    public static KeyCloakRealm adminRealm(Environment env) {
        return new KeyCloakRealm(
                env.getProperty("keycloak.server"),
                env.getProperty("keycloak.admin-realm.url"),
                env.getProperty("keycloak.admin-realm.name"),
                env.getProperty("keycloak.admin-realm.client.id"),
                env.getProperty("keycloak.admin-realm.client.secret"),
                env.getProperty("keycloak.admin-realm.username"),
                env.getProperty("keycloak.admin-realm.password")
            );
    }

}
