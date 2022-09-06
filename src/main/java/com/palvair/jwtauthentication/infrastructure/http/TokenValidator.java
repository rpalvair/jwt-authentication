package com.palvair.jwtauthentication.infrastructure.http;

import com.palvair.jwtauthentication.application.jwt.JwtTokenHelper;
import com.palvair.jwtauthentication.domain.exception.AuthentificationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class TokenValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenValidator.class);

    private final JwtTokenHelper jwtTokenHelper;

    @Autowired
    public TokenValidator(final JwtTokenHelper jwtTokenHelper) {
        this.jwtTokenHelper = jwtTokenHelper;
    }

    public Authentication validateToken(final String token) {
        try {
            return jwtTokenHelper.validateToken(token);
        } catch (final AuthentificationError exception) {
            LOGGER.error(exception.getMessage());
        }
        return null;
    }

}
