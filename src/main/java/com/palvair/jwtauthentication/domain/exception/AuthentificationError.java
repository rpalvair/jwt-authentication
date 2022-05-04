package com.palvair.jwtauthentication.domain.exception;


public abstract class AuthentificationError extends RuntimeException {


    public AuthentificationError(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public AuthentificationError(final String message) {
        super(message);
    }

}
