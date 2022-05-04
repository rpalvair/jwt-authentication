package com.palvair.jwtauthentication.domain.exception;

public class UserNotFoundException extends AuthentificationError {

    public UserNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
