package com.palvair.jwtauthentication.infrastructure.http;

import com.palvair.jwtauthentication.application.jwt.JwtTokenHelper;
import com.palvair.jwtauthentication.domain.exception.AuthentificationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenValidator.class);

    private final JwtTokenHelper jwtTokenHelper;

    @Autowired
    public TokenValidator(final JwtTokenHelper jwtTokenHelper) {
        this.jwtTokenHelper = jwtTokenHelper;
    }

    public UserDetails validateToken(final String token) {
        try {
            final UserDetails userDetails = getUserDetails();
            LOGGER.debug("userDetails in context = {}", userDetails);
            if (jwtTokenHelper.validateToken(token, userDetails)) {
                LOGGER.debug("Token is valid. userDetails = [{}]", userDetails);
                return userDetails;
            }
            LOGGER.warn("Invalid token [{}]", token);
        } catch (final AuthentificationError exception) {
            LOGGER.error(exception.getMessage());
        }
        return null;
    }

    private UserDetails getUserDetails() {
        return (UserDetails) Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(o -> o instanceof UserDetails)
                .orElse(null);
    }

}
