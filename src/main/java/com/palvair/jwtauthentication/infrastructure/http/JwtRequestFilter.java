package com.palvair.jwtauthentication.infrastructure.http;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//FIXME : ajouter tests unitaires
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final TokenValidator tokenValidator;
    private final HeaderExtractor headerExtractor;

    @Autowired
    public JwtRequestFilter(final TokenValidator tokenValidator,
                            final HeaderExtractor headerExtractor) {
        this.tokenValidator = tokenValidator;
        this.headerExtractor = headerExtractor;
    }

    //FIXME: Improve readability
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse httpServletResponse,
                                    final FilterChain filterChain) throws ServletException, IOException {
        LOGGER.debug("Requete recue");
        final String token = headerExtractor.getAuthorizationToken(request);
        if (StringUtils.isNotBlank(token)) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                final UserDetails userDetails = tokenValidator.validateToken(token);
                if (userDetails != null) {
                    updateSecurityContext(request, userDetails);
                }
            }
        }
        filterChain.doFilter(request, httpServletResponse);
    }

    private void updateSecurityContext(final HttpServletRequest request, final UserDetails userDetails) {
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        final WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);
        usernamePasswordAuthenticationToken.setDetails(details);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
