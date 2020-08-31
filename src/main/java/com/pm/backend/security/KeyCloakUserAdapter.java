package com.pm.backend.security;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.springframework.core.env.Environment;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Currently a singleton, to avoid having multiple adapters present.
 * Only need one adapter across the entire application to authenticate users
 */
public class KeyCloakUserAdapter implements  UserAuth {

    private static KeyCloakUserAdapter instance = null;

    private final SSLContext sslContext;
    private KeyCloakRealm userRealm;
    private KeyCloakRealm adminRealm;
    private AuthzClient authzClient;

    public static synchronized KeyCloakUserAdapter getInstance(UserContext context) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if(instance == null) {
            return new KeyCloakUserAdapter(context.getEnv());
        }
        return instance;
    }

    private KeyCloakUserAdapter(Environment env) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustAllStrategy()).build();
        userRealm = KeyCloakRealm.userRealm(env);
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
