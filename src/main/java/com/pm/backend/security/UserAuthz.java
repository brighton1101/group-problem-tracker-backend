package com.pm.backend.security;

import com.pm.backend.security.representations.KeyCloakException;
import com.pm.backend.security.representations.KeyCloakResource;
import com.pm.backend.security.representations.KeyCloakUser;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;

/**
 * This interface is meant to handle all user authorization
 */
public interface UserAuthz {

    public AuthorizationResponse authorize(KeyCloakUser user);

    public ResourceRepresentation createGroup(KeyCloakResource group) throws KeyCloakException;
}
