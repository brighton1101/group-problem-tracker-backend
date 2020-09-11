package com.pm.backend.security.authz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * This class, instantiated at startup, contains all the endpoints and capabilities for keycloak authz
 * Not really sure if this will be needed at all... lol
 */
@Getter
@Setter
@Accessors(chain = true)
public class KeyCloakDiscovery {
    @JsonProperty("issuer")
    String issuer;

    @JsonProperty("authorization_endpoint")
    protected String authorizationEndpoint;
    @JsonProperty("token_endpoint")
    protected String tokenEndpoint;
    @JsonProperty("introspection_endpoint")
    protected String introspectionEndpoint;
    @JsonProperty("end_session_endpoint")
    protected String endSessionEndpoint;
    @JsonProperty("jwks_uri")
    protected String jwksUri;
    @JsonProperty("grant_types_supported")
    protected List<String> grantTypesSupported;
    @JsonProperty("response_types_supported")
    protected List<String> responseTypesSupported;
    @JsonProperty("response_modes_supported")
    protected List<String> responseModesSupported;
    @JsonProperty("registration_endpoint")
    protected String registrationEndpoint;
    @JsonProperty("token_endpoint_auth_methods_supported")
    protected List<String> tokenEndpointAuthMethodsSupported;
    @JsonProperty("token_endpoint_auth_signing_alg_values_supported")
    protected List<String> tokenEndpointAuthSigningAlgValuesSupported;
    @JsonProperty("scopes_supported")
    protected List<String> scopesSupported;
    @JsonProperty("resource_registration_endpoint")
    protected String resourceRegistrationEndpoint;
    @JsonProperty("permission_endpoint")
    protected String permissionEndpoint;
    @JsonProperty("policy_endpoint")
    protected String policyEndpoint;

    @Override
    public String toString() {
        return "KeyCloakDiscovery{" +
                "issuer='" + issuer + '\'' +
                ", authorizationEndpoint='" + authorizationEndpoint + '\'' +
                ", tokenEndpoint='" + tokenEndpoint + '\'' +
                ", introspectionEndpoint='" + introspectionEndpoint + '\'' +
                ", endSessionEndpoint='" + endSessionEndpoint + '\'' +
                ", jwksUri='" + jwksUri + '\'' +
                ", grantTypesSupported=" + grantTypesSupported +
                ", responseTypesSupported=" + responseTypesSupported +
                ", responseModesSupported=" + responseModesSupported +
                ", registrationEndpoint='" + registrationEndpoint + '\'' +
                ", tokenEndpointAuthMethodsSupported=" + tokenEndpointAuthMethodsSupported +
                ", tokenEndpointAuthSigningAlgValuesSupported=" + tokenEndpointAuthSigningAlgValuesSupported +
                ", scopesSupported=" + scopesSupported +
                ", resourceRegistrationEndpoint='" + resourceRegistrationEndpoint + '\'' +
                ", permissionEndpoint='" + permissionEndpoint + '\'' +
                ", policyEndpoint='" + policyEndpoint + '\'' +
                '}';
    }
}
