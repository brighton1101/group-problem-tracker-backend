package com.pm.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.backend.security.authz.KeyCloakAuthzAdapter;
import com.pm.backend.security.representations.*;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import javax.net.ssl.SSLContext;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.pm.backend.security.representations.KeyCloakException.REASON.USER_CREATION_HTTP_FAILURE;
import static org.keycloak.OAuth2Constants.*;

/**
 * Currently a singleton, to avoid having multiple adapters present.
 * Only need one adapter across the entire application to authenticate users
 */
@Component
public class KeyCloakUserAdapter implements UserAuth {

    private Logger logger = LoggerFactory.getLogger(KeyCloakUserAdapter.class);

    private static KeyCloakUserAdapter instance = null;

    private final SSLContext sslContext;
    private KeyCloakRealm userRealm;
    private KeyCloakRealm adminRealm;


    //TODO move this out into its own singleton
    private KeyCloakAuthzAdapter keyCloakAuthzAdapter;

    public static synchronized KeyCloakUserAdapter getInstance(KeyCloakContext context) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if (instance == null) {
            instance = new KeyCloakUserAdapter(context.getEnv());
        }
        return instance;
    }

    private KeyCloakUserAdapter(Environment env) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustAllStrategy()).build();
        userRealm = KeyCloakRealm.userRealm(env);
        adminRealm = KeyCloakRealm.adminRealm(env);
        keyCloakAuthzAdapter = KeyCloakAuthzAdapter.getInstance(userRealm);


    }






    @Override
    public Optional<AccessToken> login(KeyCloakUser user) {
        AccessToken at = null;
        try {
            AuthorizationResponse authorizationResponse = keyCloakAuthzAdapter.authorize(user);
            at = convertResponseToToken(authorizationResponse);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return Optional.of(at);

    }

    @Override
    public Optional<AccessToken> refresh(String refreshToken) {
        String url = userRealm.getRealmUrl() + "/protocol/openid-connect/token";
        Map<String, String> params = new HashMap<>();

        params.put(CLIENT_ID, userRealm.getClientId());
        params.put(CLIENT_SECRET, userRealm.getClientSecret());
        params.put(GRANT_TYPE, REFRESH_TOKEN);
        params.put(REFRESH_TOKEN, refreshToken);
        String tokenString;
        try {
            tokenString = HttpUtils.postFormRequest(url, sslContext, params);
        } catch (Exception e) {
            return Optional.empty();
        }
        ObjectMapper om = new ObjectMapper();
        AccessToken token = null;
        try {
            token = om.readValue(tokenString, AccessToken.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.of(token);
    }

    /**
     *
     * @param userId This is the keycloak-provided userId, NOT the username
     * @throws KeyCloakException
     */
    @Override
    public boolean logout(String userId) {
        try {
            Keycloak keycloak = getAdminClient();
            UserResource userResource = getUserResource(keycloak, userId);
            userResource.logout();
            keycloak.close();
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return false;
    }

    /**
     *
     * @param registerUser
     * @throws Exception
     *
     * Currently assumes all users will be added to the same userRealm
     */
    @Override
    public Optional<KeyCloakUser> register(KeyCloakUser registerUser) {
        //TODO add some validation

        KeyCloakUser keyCloakUser = null;
        Keycloak keycloak = getAdminClient();
        RealmResource userRealmResource = keycloak.realm(userRealm.getRealmName());
        UsersResource usersResource = userRealmResource.users();
        if (userExists(registerUser, usersResource)) {
            return Optional.empty();
        }
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(registerUser.getUserName());
        userRepresentation.setEmail(registerUser.getEmail());
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(registerUser.getPassword());
        userRepresentation.setCredentials(Arrays.asList(credentialRepresentation));
        userRepresentation.setEnabled(true);
        try(Response response = usersResource.create(userRepresentation)) {
            if(response.getStatus()==201) {
                String[] pathArr = response.getLocation().getPath().split("/");
                String userId = pathArr[pathArr.length-1];
                RoleRepresentation userRole = userRealmResource.roles().get("user").toRepresentation();
                List<RoleRepresentation> roles =  new ArrayList<>();
                roles.add(userRole);
                userRealmResource.users().get(userId).roles().realmLevel()
                        .add(roles);
                keyCloakUser = registerUser.copy();
                keyCloakUser.setId(userId);
                UserResource userResource = usersResource.get(userId);
                keyCloakUser.setTimeCreated(new Date(userResource.toRepresentation().getCreatedTimestamp()));
                }
            else {
                logger.info(response.toString());
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
        keycloak.close();
        return Optional.of(keyCloakUser);
    }

    public Optional<KeyCloakUser> getUserById(String id) {
        KeyCloakUser res = null;
        try {
            Keycloak keycloak = getAdminClient();
            UserResource userResource = getUserResource(keycloak, id);
            res = convertResourceToUser(keycloak, userResource);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return Optional.of(res);
    }


    public Optional<KeyCloakUser> getUserByName(String userName) {
        Keycloak keycloak = getAdminClient();
        List<UserRepresentation> userReps = keycloak.realm(userRealm.getRealmName()).users().search(userName);
        if(userReps == null || userReps.isEmpty()) {
            return Optional.empty();
        }
        KeyCloakUser user = new KeyCloakUser(userReps.get(0));
        try {
            List<RoleRepresentation> roleRepresentations = getUserResource(keycloak, user.getId()).roles().realmLevel().listEffective();
            if(!roleRepresentations.isEmpty()) {
                List<String> roles = new ArrayList<>();
                for(RoleRepresentation roleRep : roleRepresentations) {
                    roles.add(roleRep.getName());
                }
                user.setRoles(roles);
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Optional.empty();
        }
        return Optional.of(user);
    }

    private boolean userExists(KeyCloakUser user, UsersResource usersResource) {
        List users = usersResource.search(user.getUserName());
        return !users.isEmpty();
    }

    private UserResource getUserResource(Keycloak keycloak, String userId) throws KeyCloakException {



        UserResource userResource = keycloak.realm(userRealm.getRealmName()).users().get(userId);

        //try doing something with the userResource, to make sure it works
        try {
            logger.info("Got here1");
            userResource.toRepresentation();
            logger.info("Got here2");
        }
        catch (Exception e) {
            throw new KeyCloakException(e, KeyCloakException.REASON.USER_DOESNT_EXIST);
        }
        return userResource;
    }

    private KeyCloakUser convertResourceToUser(Keycloak keycloak, UserResource userResource) {
        KeyCloakUser user = new KeyCloakUser(userResource.toRepresentation());

        List<RoleRepresentation> roleRepresentations = userResource.roles().realmLevel().listEffective();
        if(!roleRepresentations.isEmpty()) {
            List<String> roles = new ArrayList<>();
            for(RoleRepresentation roleRep : roleRepresentations) {
                roles.add(roleRep.getName());
            }
            user.setRoles(roles);
        }

        return user;

    }

    private AccessToken convertResponseToToken(AccessTokenResponse accessTokenResponse) {
        return new AccessToken()
                .setToken(accessTokenResponse.getToken())
                .setRefreshToken(accessTokenResponse.getRefreshToken())
                .setExpiresIn(accessTokenResponse.getExpiresIn())
                .setRefreshExpiresIn(accessTokenResponse.getRefreshExpiresIn())
                .setTokenType(accessTokenResponse.getTokenType())
                .setIdToken(accessTokenResponse.getIdToken())
                .setNotBeforePolicy(accessTokenResponse.getNotBeforePolicy())
                .setSessionState(accessTokenResponse.getSessionState())
                .setScope(accessTokenResponse.getScope());
    }

    private Keycloak getAdminClient() {
        return Keycloak.getInstance(
                adminRealm.getServerUrl(),
                adminRealm.getRealmName(),
                adminRealm.getAdminName(),
                adminRealm.getAdminPassword(),
                adminRealm.getClientId(),
                adminRealm.getClientSecret(),
                sslContext
        );
    }
}
