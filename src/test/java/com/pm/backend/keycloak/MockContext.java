package com.pm.backend.keycloak;

import org.mockito.Mockito;
import org.springframework.core.env.Environment;

public class MockContext {

    public static Environment getMockContext() {
        Environment env = Mockito.mock(Environment.class);

        Mockito.when(env.getProperty("keycloak.server")).thenReturn("http://localhost:8180/auth");
        Mockito.when(env.getProperty("keycloak.user-realm.url")).thenReturn("http://localhost:8180/auth/realms/ProblemTracker");
        Mockito.when(env.getProperty("keycloak.user-realm.name")).thenReturn("ProblemTracker");
        Mockito.when(env.getProperty("keycloak.user-realm.client.id")).thenReturn("login-app");
        Mockito.when(env.getProperty("keycloak.user-realm.client.secret")).thenReturn("850f1b8a-6310-481c-a07d-c974c2640d43");

        Mockito.when(env.getProperty("keycloak.admin-realm.url")).thenReturn("http://localhost:8180/auth/realms/ProblemTracker");
        Mockito.when(env.getProperty("keycloak.admin-realm.name")).thenReturn("ProblemTracker");
        Mockito.when(env.getProperty("keycloak.admin-realm.username")).thenReturn("misstracy");
        Mockito.when(env.getProperty("keycloak.admin-realm.password")).thenReturn("michael123");
        Mockito.when(env.getProperty("keycloak.admin-realm.client.id")).thenReturn("admin-cli");
        Mockito.when(env.getProperty("keycloak.admin-realm.client.secret")).thenReturn( "f1f9e1f8-c2cb-4cfc-aad0-9cbbbebb3853");


        return env;
    }
}
