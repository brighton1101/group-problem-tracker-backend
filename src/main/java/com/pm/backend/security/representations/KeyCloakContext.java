package com.pm.backend.security.representations;

import org.springframework.core.env.Environment;

public class KeyCloakContext {

    public Environment getEnv() {
        return env;
    }

    private final Environment env;

    public KeyCloakContext(Environment env) {
        this.env = env;
    }
}
