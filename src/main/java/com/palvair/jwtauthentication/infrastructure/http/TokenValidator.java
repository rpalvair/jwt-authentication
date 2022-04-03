package com.palvair.jwtauthentication.infrastructure.http;

import com.palvair.jwtauthentication.application.jwt.JwtTokenHelper;
import com.palvair.jwtauthentication.application.jwt.JwtUserDetailsService;
import com.palvair.jwtauthentication.domain.exception.AuthentificationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenValidator.class);

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenHelper jwtTokenHelper;

    @Autowired
    public TokenValidator(final JwtUserDetailsService jwtUserDetailsService,
                          final JwtTokenHelper jwtTokenHelper) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    public UserDetails validateToken(final String token) {
        try {
            final String username = jwtTokenHelper.getUsernameFromToken(token);
            final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenHelper.validateToken(token, userDetails)) {
                LOGGER.debug("Token valide. userDetails = [{}]", userDetails);
                return userDetails;
            }
            LOGGER.warn("Token invalide [{}]", token);
        } catch (final AuthentificationError exception) {
            LOGGER.error(exception.getMessage());
        }
        return null;
    }

}
