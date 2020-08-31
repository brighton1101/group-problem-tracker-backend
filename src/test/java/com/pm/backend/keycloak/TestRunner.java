package com.pm.backend.keycloak;

import org.junit.runner.JUnitCore;

public class TestRunner {

    public static void main(String[] args) {
        JUnitCore.runClasses(KeyCloakUserTests.class);
    }
}
