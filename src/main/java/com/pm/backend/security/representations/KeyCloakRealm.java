package com.pm.backend.security.representations;

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
                env.getProperty("kc.server"),
                env.getProperty("kc.user-realm.url"),
                env.getProperty("kc.user-realm.name"),
                env.getProperty("kc.user-realm.client.id"),
                env.getProperty("kc.user-realm.client.secret"),
                null,
                null);
    }

    public static KeyCloakRealm adminRealm(Environment env) {
        return new KeyCloakRealm(
                env.getProperty("kc.server"),
                env.getProperty("kc.admin-realm.url"),
                env.getProperty("kc.admin-realm.name"),
                env.getProperty("kc.admin-realm.client.id"),
                env.getProperty("kc.admin-realm.client.secret"),
                env.getProperty("kc.admin-realm.username"),
                env.getProperty("kc.admin-realm.password")
            );
    }

}
