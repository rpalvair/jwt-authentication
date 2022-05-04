package com.palvair.jwtauthentication.presentation.error;


public class AuthenticationBadRequest {

    private final String message;

    public AuthenticationBadRequest(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
