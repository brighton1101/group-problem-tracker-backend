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
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.parameters.P;

import javax.net.ssl.SSLContext;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Currently a singleton, to avoid having multiple adapters present.
 * Only need one adapter across the entire application to authenticate users
 */
public class KeyCloakUserAdapter implements UserAuth {

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
    public AccessToken login(String user, String password) throws Exception {
        AuthorizationRequest request = new AuthorizationRequest();
        AuthorizationResponse authorizationResponse;


        try {
            authorizationResponse = authzClient.authorization(user, password).authorize(request);
        } catch (Exception e) {
            throw e;
        }

        return convertResponseToToken(authorizationResponse);
    }

    @Override
    public AccessToken refresh(String refreshToken) throws Exception {
        return null;
    }

    @Override
    public void logout(String user) throws Exception {

    }

    /**
     *
     * @param registerUser
     * @throws Exception
     *
     * Currently assumes all users will be added to the same userRealm
     */
    @Override
    public KeyCloakUser register(KeyCloakUser registerUser) throws Exception {
        //TODO add some validation

        KeyCloakUser keyCloakUser = null;

        Keycloak keycloak = getAdminClient();


        RealmResource userRealmResource = keycloak.realm(userRealm.getRealmName());
        UsersResource usersResource = userRealmResource.users();

        if(!userExists(registerUser, usersResource)) {
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(registerUser.getUserName());
            userRepresentation.setEmail(registerUser.getEmail());

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(registerUser.getPassword());
            userRepresentation.setCredentials(Arrays.asList(credentialRepresentation));


            try(Response response = usersResource.create(userRepresentation)) {
                if(response.getStatus()==201) {
                    String[] pathArr = response.getLocation().getPath().split("/");
                    String userId = pathArr[pathArr.length-1];
                    keyCloakUser = registerUser.copy();
                    keyCloakUser.setId(userId);

                    UserResource userResource = usersResource.get(userId);
                    keyCloakUser.setTimeCreated(new Date(userResource.toRepresentation().getCreatedTimestamp()));

                }
                else {
                    //bad news, either throw a custom exception, or just return null for user

                    throw new Exception();

                }

            }


        }

        keycloak.close();

        return keyCloakUser;
    }

    private boolean userExists(KeyCloakUser user, UsersResource usersResource) {
        List users = usersResource.search(user.getUserName());
        return !users.isEmpty();
    }

    private AccessToken convertResponseToToken(AccessTokenResponse accessTokenResponse) {
        AccessToken accessToken = new AccessToken();

        //TODO add additional fields as necessary
        accessToken.setToken(accessTokenResponse.getToken());
        accessToken.setRefreshToken(accessTokenResponse.getRefreshToken());

        return accessToken;
    }

    private Keycloak getAdminClient() {
        return Keycloak.getInstance(
                adminRealm.getServerUrl(),
                adminRealm.getRealmName(),
                adminRealm.getAdminName(),
                adminRealm.getAdminPassword(),
                adminRealm.getClientId(),
                null,
                sslContext
        );
    }
}
