package com.pm.backend.security.authz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.backend.security.HttpUtils;
import com.pm.backend.security.UserAuthz;
import com.pm.backend.security.representations.KeyCloakRealm;
import com.pm.backend.security.representations.KeyCloakUser;
import com.pm.backend.security.representations.UserException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.pm.backend.security.representations.UserException.REASON.AUTHZ_NOT_INITIALIZED;

/**
 * Currently a singleton, to avoid having multiple adapters present.
 * Only need one adapter across the entire application to authorize users
 */
public class KeyCloakAuthzAdapter implements UserAuthz {

    private Logger logger = LoggerFactory.getLogger(KeyCloakAuthzAdapter.class);

    private static  KeyCloakAuthzAdapter instance = null;
    private final SSLContext sslContext;
    private AuthzClient authzClient;
    private KeyCloakRealm userRealm;

    public static synchronized KeyCloakAuthzAdapter getInstance(KeyCloakRealm realm) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if(instance == null) {
            instance = new KeyCloakAuthzAdapter(realm);
        }
        return instance;

    }
    public static synchronized KeyCloakAuthzAdapter getInstance() throws UserException {
        if(instance == null) {
            throw new UserException(AUTHZ_NOT_INITIALIZED);
        }
        return instance;

    }

    private KeyCloakAuthzAdapter(KeyCloakRealm realm) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustAllStrategy()).build();
        authzClient = createAuthzClient(realm);
        userRealm = realm;

        this.discovery();

    }

    private void discovery() {
        String url = userRealm.getRealmUrl() + "/.well-known/uma2-configuration";
        try {
            String responseString = HttpUtils.getRequest(url, sslContext);
            logger.info("Response: {}", responseString);
            ObjectMapper om = new ObjectMapper();
            Map<String, Object> responseMap = om.readValue(responseString, Map.class);
            responseMap.forEach((k,v) -> logger.info("Key = "
                    + k + ", Value = " + v));
        }catch (Exception e) {
            logger.error("Exception in initializing authz " + e.getMessage());
            e.printStackTrace();
        }


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
    public AuthorizationResponse authorize(KeyCloakUser user) {
        AuthorizationRequest request = new AuthorizationRequest();
        AuthorizationResponse authorizationResponse;

        try {
            authorizationResponse = authzClient.authorization(user.getUserName(), user.getPassword()).authorize(request);
            return authorizationResponse;
        } catch (Exception e) {
            throw e;
        }
    }
}
