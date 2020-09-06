package com.pm.backend.security;

import org.apache.catalina.User;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.net.ssl.SSLContext;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.pm.backend.security.UserException.REASON.USER_CREATION_HTTP_FAILURE;

/**
 * Currently a singleton, to avoid having multiple adapters present.
 * Only need one adapter across the entire application to authenticate users
 */
public class KeyCloakUserAdapter implements UserAuth {

    private Logger logger = LoggerFactory.getLogger(KeyCloakUserAdapter.class);

    private static KeyCloakUserAdapter instance = null;

    private final SSLContext sslContext;
    private KeyCloakRealm userRealm;
    private KeyCloakRealm adminRealm;
    private AuthzClient authzClient;

    public static synchronized KeyCloakUserAdapter getInstance(UserContext context) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if (instance == null) {
            return new KeyCloakUserAdapter(context.getEnv());
        }
        return instance;
    }

    private KeyCloakUserAdapter(Environment env) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustAllStrategy()).build();
        userRealm = KeyCloakRealm.userRealm(env);
        adminRealm = KeyCloakRealm.adminRealm(env);
        authzClient = createAuthzClient(userRealm);


    }

    private AuthzClient createAuthzClient(KeyCloakRealm realm) {
        Map<String, Object> secretsMap = new HashMap<>();
        secretsMap.put("secret", realm.getClientSecret());


        //This creates an HttpClient which will trust all certificates
        CloseableHttpClient keyCloakHttpClient = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier())).build();

        Configuration keyCloakConfig = new Configuration(
                realm.getServerUrl(),
                realm.getRealmName(),
                realm.getClientId(),
                secretsMap, keyCloakHttpClient
        );
        return AuthzClient.create(keyCloakConfig);
    }




    @Override
    public AccessToken login(KeyCloakUser user) throws Exception {
        AuthorizationRequest request = new AuthorizationRequest();
        AuthorizationResponse authorizationResponse;


        try {
            authorizationResponse = authzClient.authorization(user.getUserName(), user.getPassword()).authorize(request);
        } catch (Exception e) {
            throw e;
        }

        return convertResponseToToken(authorizationResponse);
    }

    @Override
    public AccessToken refresh(String refreshToken) throws Exception {
        return null;
    }




    /**
     *
     * @param userId This is the keycloak-provided userId, NOT the username
     * @throws UserException
     */
    @Override
    public void logout(String userId) throws UserException {
        Keycloak keycloak = getAdminClient();

        UserResource userResource = getUserResource(keycloak, userId);

        logger.info("userResource: {}", userResource);

        userResource.logout();
        keycloak.close();
    }

    /**
     *
     * @param registerUser
     * @throws Exception
     *
     * Currently assumes all users will be added to the same userRealm
     */
    @Override
    public KeyCloakUser register(KeyCloakUser registerUser) throws UserException {
        //TODO add some validation

        KeyCloakUser keyCloakUser = null;

        Keycloak keycloak = getAdminClient();


        RealmResource userRealmResource = keycloak.realm(userRealm.getRealmName());
        //logger.info("userRealmresource clients: {}", userRealmResource.clients());
        UsersResource usersResource = userRealmResource.users();
        //logger.info("users resource {}", usersResource.list());

        if(!userExists(registerUser, usersResource)) {
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(registerUser.getUserName());
            userRepresentation.setEmail(registerUser.getEmail());

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(registerUser.getPassword());
            userRepresentation.setCredentials(Arrays.asList(credentialRepresentation));


            try(Response response = usersResource.create(userRepresentation)) {

                logger.info("response: {}", response);
                if(response.getStatus()==201) {
                    String[] pathArr = response.getLocation().getPath().split("/");
                    String userId = pathArr[pathArr.length-1];
                    keyCloakUser = registerUser.copy();
                    keyCloakUser.setId(userId);

                    UserResource userResource = usersResource.get(userId);
                    keyCloakUser.setTimeCreated(new Date(userResource.toRepresentation().getCreatedTimestamp()));
                    
                    //TODO the user roles should also be added to the returned keycloakuser

                }
                else {
                    //bad news, either throw a custom exception

                    throw new UserException(new Exception(), USER_CREATION_HTTP_FAILURE);

                }

            }


        }
        else {
            //user already exists
            throw new UserException(UserException.REASON.USER_ALREADY_EXISTS);
        }

        keycloak.close();

        return keyCloakUser;
    }

    public KeyCloakUser getUserById(String id) throws UserException {
        Keycloak keycloak = getAdminClient();
        UserResource userResource = getUserResource(keycloak, id);
        return convertResourceToUser(keycloak, userResource);
    }


    public KeyCloakUser getUserByName(String userName) throws UserException {
        Keycloak keycloak = getAdminClient();
        List<UserRepresentation> userReps = keycloak.realm(userRealm.getRealmName()).users().search(userName);

        if(userReps == null || userReps.isEmpty()) {
            throw new UserException(UserException.REASON.USER_DOESNT_EXIST);
        }

        KeyCloakUser user = new KeyCloakUser(userReps.get(0));

        List<RoleRepresentation> roleRepresentations = getUserResource(keycloak, user.getId()).roles().realmLevel().listEffective();
        if(!roleRepresentations.isEmpty()) {
            List<String> roles = new ArrayList<>();
            for(RoleRepresentation roleRep : roleRepresentations) {
                roles.add(roleRep.getName());
            }
            user.setRoles(roles);
        }

        return user;
    }

    private boolean userExists(KeyCloakUser user, UsersResource usersResource) {
        List users = usersResource.search(user.getUserName());
        return !users.isEmpty();
    }

    private UserResource getUserResource(Keycloak keycloak, String userId) throws UserException{



        UserResource userResource = keycloak.realm(userRealm.getRealmName()).users().get(userId);

        //try doing something with the userResource, to make sure it works
        try {
            logger.info("Got here1");
            userResource.toRepresentation();
            logger.info("Got here2");
        }
        catch (Exception e) {
            throw new UserException(e, UserException.REASON.USER_DOESNT_EXIST);
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
