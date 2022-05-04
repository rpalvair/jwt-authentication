package com.palvair.jwtauthentication.application.jwt;

public class JwtResponse {

    private final String jwtToken;

    public JwtResponse(final String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getToken() {
        return jwtToken;
    }
}
