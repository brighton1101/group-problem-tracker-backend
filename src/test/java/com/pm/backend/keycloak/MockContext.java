package com.pm.backend.keycloak;

import com.pm.backend.security.UserContext;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

public class MockContext {

    public static Environment getMockContext() {
        Environment env = Mockito.mock(Environment.class);

        Mockito.when(env.getProperty("keycloak.server")).thenReturn("http://localhost:8180/auth");
        Mockito.when(env.getProperty("keycloak.user-realm.url")).thenReturn("http://localhost:8180/auth/realms/ProblemTracker");
        Mockito.when(env.getProperty("keycloak.user-realm.name")).thenReturn("ProblemTracker");
        Mockito.when(env.getProperty("keycloak.user-realm.client.id")).thenReturn("login-app");
        Mockito.when(env.getProperty("keycloak.user-realm.client.secret")).thenReturn("479eeef9-e332-42ad-bdfe-3651272f640a");

        Mockito.when(env.getProperty("keycloak.admin-realm.url")).thenReturn("http://localhost:8180/auth/realms/ProblemTracker");
        Mockito.when(env.getProperty("keycloak.admin-realm.name")).thenReturn("ProblemTracker");
        Mockito.when(env.getProperty("keycloak.admin-realm.username")).thenReturn("misstracy");
        Mockito.when(env.getProperty("keycloak.admin-realm.password")).thenReturn("michael123");
        Mockito.when(env.getProperty("keycloak.admin-realm.client.id")).thenReturn("admin-client");
        Mockito.when(env.getProperty("keycloak.admin-realm.client.secret")).thenReturn( "d378b422-241a-4bb9-850f-99df6bccf701");


        return env;
    }
}
