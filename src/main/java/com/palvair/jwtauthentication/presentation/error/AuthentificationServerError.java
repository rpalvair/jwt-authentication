package com.palvair.jwtauthentication.presentation.error;


public class AuthentificationServerError {

    private final String message;

    public AuthentificationServerError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
