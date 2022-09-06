package com.palvair.jwtauthentication.domain.exception;


public class AuthentificationException extends AuthentificationError {


    public AuthentificationException(final String message) {
        super(message);
    }

    public AuthentificationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}


