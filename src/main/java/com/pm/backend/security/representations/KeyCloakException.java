package com.pm.backend.security.representations;

import lombok.Getter;
import lombok.Setter;

/**
 * A wrapper around Exception for user related exceptions
 * might not be used...
 */
@Getter
@Setter
public class KeyCloakException extends Exception {



    public enum REASON {
        USER_ALREADY_EXISTS,
        USER_CREATION_HTTP_FAILURE,
        USER_DOESNT_EXIST,
        HTTP_POST_FAIL,
        HTTP_GET_FAIL,
        AUTHZ_NOT_INITIALIZED,
        RESOURCE_EXISTS,
        GROUP_EXISTS,
        GROUP_CHECK_PERMISSION_FAILURE,

        UNKNOWN_REASON;
    }

    private REASON reason;
    private Exception baseException;

    public KeyCloakException(REASON reason) {
        super();
        this.reason = reason;
    }

    public KeyCloakException(Exception e, REASON reason) {
        super(e);
        this.reason = reason;
        this.baseException = e;
    }

    @Override
    public String toString() {
        return "KeyCloakException{" +
                "reason=" + reason +
                ", baseException=" + baseException.getCause() +
                '}';
    }
}
