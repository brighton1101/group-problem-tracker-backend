package com.pm.backend.security;

public interface UserAuth {

    public AccessToken login(String user, String password) throws Exception;

    public AccessToken refresh(String refreshCode) throws Exception;

    public void logout(String user) throws Exception;

    public void register(String user, String email, String password) throws Exception;
}
