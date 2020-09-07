package com.pm.backend.security;

import com.pm.backend.security.representations.AccessToken;
import com.pm.backend.security.representations.KeyCloakUser;

public interface UserAuth {

    public AccessToken login(KeyCloakUser user) throws Exception;

    public AccessToken refresh(String refreshCode) throws Exception;

    public void logout(String user) throws Exception;

    public KeyCloakUser register(KeyCloakUser registerUser) throws Exception;
}
