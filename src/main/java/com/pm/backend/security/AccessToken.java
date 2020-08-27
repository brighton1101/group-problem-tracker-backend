package com.pm.backend.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessToken {
    @JsonProperty("access_token")
    protected String token;

    @JsonProperty("expires_in")
    protected long expiresIn;

    @JsonProperty("refresh_expires_in")
    protected long refreshExpiresIn;

    @JsonProperty("refresh_token")
    protected String refreshToken;

    @JsonProperty("token_type")
    protected String tokenType;

    @JsonProperty("scope")
    protected String scope;
}
