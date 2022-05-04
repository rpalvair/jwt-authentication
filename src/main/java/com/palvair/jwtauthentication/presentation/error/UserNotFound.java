package com.palvair.jwtauthentication.presentation.error;


public class UserNotFound {

    private final String message;

    public UserNotFound(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
