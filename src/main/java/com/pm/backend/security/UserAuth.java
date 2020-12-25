package com.pm.backend.security;

import com.pm.backend.security.representations.AccessToken;
import com.pm.backend.security.representations.KeyCloakException;
import com.pm.backend.security.representations.KeyCloakUser;

import java.util.Optional;

/**
 * This interface is meant to handle all user authentication
 */
public interface UserAuth {

    public Optional<AccessToken> login(KeyCloakUser user);

    public Optional<AccessToken> refresh(String refreshCode);

    public boolean logout(String user);

    public Optional<KeyCloakUser> register(KeyCloakUser registerUser);

    public Optional<KeyCloakUser> getUserById(String id);

    public Optional<KeyCloakUser> getUserByName(String userName) throws Exception;
}
