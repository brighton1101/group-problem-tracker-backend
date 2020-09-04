package com.pm.backend.security;

public interface UserAuth {

    public AccessToken login(KeyCloakUser user) throws Exception;

    public AccessToken refresh(String refreshCode) throws Exception;

    public void logout(String user) throws Exception;

    public KeyCloakUser register(KeyCloakUser registerUser) throws Exception;
}
