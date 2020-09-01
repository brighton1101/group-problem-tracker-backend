package com.pm.backend.security;

import lombok.Getter;
import lombok.Setter;

/**
 * A wrapper around Exception for user related exceptions
 * might not be used...
 */
@Getter
@Setter
public class UserException extends Exception {

    private String reason;
    private Exception baseException;

    public UserException(Exception e, String reason) {
        super(e);
        this.reason = reason;
    }
}
