package com.pm.backend.keycloak;

import com.pm.backend.security.UserContext;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

public class MockContext {

    public static Environment getMockContext() {
        Environment env = Mockito.mock(Environment.class);

        Mockito.when(env.getProperty("keycloak.server")).thenReturn("http://localhost:8180/auth");
        Mockito.when(env.getProperty("keycloak.user-realm.url")).thenReturn("http://localhost:8180/auth/realms/testrealm");
        Mockito.when(env.getProperty("keycloak.user-realm.name")).thenReturn("testrealm");
        Mockito.when(env.getProperty("keycloak.user-realm.client.id")).thenReturn("login-app");
        Mockito.when(env.getProperty("keycloak.user-realm.client.secret")).thenReturn("479eeef9-e332-42ad-bdfe-3651272f640a");


        return env;
    }
}
