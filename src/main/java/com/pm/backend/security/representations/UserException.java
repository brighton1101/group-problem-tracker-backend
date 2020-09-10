package com.pm.backend.security.representations;

import lombok.Getter;
import lombok.Setter;

/**
 * A wrapper around Exception for user related exceptions
 * might not be used...
 */
@Getter
@Setter
public class UserException extends Exception {



    public enum REASON {
        USER_ALREADY_EXISTS,
        USER_CREATION_HTTP_FAILURE,
        USER_DOESNT_EXIST,
        HTTP_POST_FAIL,
        HTTP_GET_FAIL,
        AUTHZ_NOT_INITIALIZED,
        UNKNOWN_REASON;
    }

    private REASON reason;
    private Exception baseException;

    public UserException(REASON reason) {
        super();
        this.reason = reason;
    }

    public UserException(Exception e, REASON reason) {
        super(e);
        this.reason = reason;
        this.baseException = e;
    }

    @Override
    public String toString() {
        return "UserException{" +
                "reason=" + reason +
                ", baseException=" + baseException.getCause() +
                '}';
    }
}
