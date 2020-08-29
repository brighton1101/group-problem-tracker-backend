package com.pm.backend.security;

import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Currently a singleton, to avoid having multiple adapters present.
 * Only need one adapter across the entire application to authenticate users
 */
public class KeyCloakUserAdapter implements  UserAuth {

    private static KeyCloakUserAdapter instance = null;

    private final SSLContext sslContext;

    private AuthzClient authzClient;

    private KeyCloakUserAdapter() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustAllStrategy()).build();
        authzClient = AuthzClient.create();
    }

    public static synchronized KeyCloakUserAdapter getInstance() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if(instance == null) {
            return new KeyCloakUserAdapter();
        }
        return instance;
    }



    @Override
    public AccessToken login(String user, String password) throws Exception {
        AuthorizationRequest request = new AuthorizationRequest();

        AuthorizationResponse authorizationResponse = authzClient.authorization(user, password).authorize(request);

        return convertResponseToToken(authorizationResponse);
    }

    @Override
    public AccessToken refresh(String refreshCode) throws Exception {
        return null;
    }

    @Override
    public void logout(String user) throws Exception {

    }

    @Override
    public void register(String user, String email, String password) throws Exception {

    }

    private AccessToken convertResponseToToken(AccessTokenResponse accessTokenResponse) {
        AccessToken accessToken = new AccessToken();

        //TODO add additional fields as necessary
        accessToken.setToken(accessTokenResponse.getToken());

        return accessToken;
    }
}
