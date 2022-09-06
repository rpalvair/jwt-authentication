package com.palvair.jwtauthentication.infrastructure.http;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class HeaderExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderExtractor.class);
    private static final String BEARER = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    public String getAuthorizationToken(final HttpServletRequest request) {
        final String authorization = request.getHeader(HEADER_AUTHORIZATION);
        LOGGER.debug("Authorization header {}", authorization);
        if (StringUtils.isNotBlank(authorization) && authorization.startsWith(BEARER)) {
            final String token = authorization.substring(BEARER.length());
            LOGGER.debug("Token extracted from Authorization header [{}]", token);
            return token;
        }
        LOGGER.warn("Bad format for Authorization header");
        return null;
    }
}
