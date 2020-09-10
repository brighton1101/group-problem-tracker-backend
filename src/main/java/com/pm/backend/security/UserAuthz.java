package com.pm.backend.security;

import com.pm.backend.security.representations.KeyCloakUser;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

/**
 * This interface is meant to handle all user authorization
 */
public interface UserAuthz {

    public AuthorizationResponse authorize(KeyCloakUser user);
}
