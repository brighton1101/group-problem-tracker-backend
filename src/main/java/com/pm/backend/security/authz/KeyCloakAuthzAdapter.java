package com.pm.backend.security.authz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.backend.security.HttpUtils;
import com.pm.backend.security.UserAuthz;
import com.pm.backend.security.representations.KeyCloakException;
import com.pm.backend.security.representations.KeyCloakResource;
import com.pm.backend.security.representations.KeyCloakRealm;
import com.pm.backend.security.representations.KeyCloakUser;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.resource.ProtectedResource;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.pm.backend.security.representations.KeyCloakException.REASON.*;

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
    private KeyCloakDiscovery keyCloakDiscovery;

    public static synchronized KeyCloakAuthzAdapter getInstance(KeyCloakRealm realm) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if(instance == null) {
            instance = new KeyCloakAuthzAdapter(realm);
        }
        return instance;

    }
    public static synchronized KeyCloakAuthzAdapter getInstance() throws KeyCloakException {
        if(instance == null) {
            throw new KeyCloakException(AUTHZ_NOT_INITIALIZED);
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
            /*Map<String, Object> responseMap = om.readValue(responseString, Map.class);
            responseMap.forEach((k,v) -> logger.info("Key = "
                    + k + ", Value = " + v));*/
            keyCloakDiscovery = om.readValue(responseString, KeyCloakDiscovery.class);
            logger.info("Discovery success: " + keyCloakDiscovery);
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

    @Override
    public ResourceRepresentation createGroup(KeyCloakResource keyCloakResource) throws KeyCloakException {
        ResourceRepresentation newResource = new ResourceRepresentation();

        newResource.setName(keyCloakResource.getName());
        newResource.setType(keyCloakResource.getType());
        newResource.setUris(keyCloakResource.getUris());
        newResource.setOwner(keyCloakResource.getOwnerId());
        newResource.setOwnerManagedAccess(true);

        Set<ScopeRepresentation> scopeRepresentationSet = keyCloakResource.getScopes().stream().map(ScopeRepresentation::new).collect(Collectors.toSet());
        newResource.setScopes(scopeRepresentationSet);
        try {
            return createResource(newResource);
        } catch (KeyCloakException e) {
            if(e.getReason() == RESOURCE_EXISTS) throw new KeyCloakException(GROUP_EXISTS);
            throw e;
        }

    }

    private ResourceRepresentation createResource(ResourceRepresentation newResource) throws KeyCloakException{


        ProtectedResource resourceClient = authzClient.protection().resource();
        ResourceRepresentation existingResource = resourceClient.findByName(newResource.getName());
        if (existingResource != null) {
            throw new KeyCloakException(RESOURCE_EXISTS);
        }
        // create the resource on the server
        ResourceRepresentation response = resourceClient.create(newResource);
        String resourceId = response.getId();

        // query the resource using its newly generated id
        ResourceRepresentation resource = resourceClient.findById(resourceId);

        return resource;
    }
}
