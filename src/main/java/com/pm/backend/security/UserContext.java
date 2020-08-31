package com.pm.backend.security;

import org.springframework.core.env.Environment;

public class UserContext {

    public Environment getEnv() {
        return env;
    }

    private final Environment env;

    public UserContext(Environment env) {
        this.env = env;
    }
}
