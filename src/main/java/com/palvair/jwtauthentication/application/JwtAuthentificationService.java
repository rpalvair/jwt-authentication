package com.palvair.jwtauthentication.application;

import com.palvair.jwtauthentication.application.jwt.JwtResponse;
import com.palvair.jwtauthentication.application.jwt.JwtTokenHelper;
import com.palvair.jwtauthentication.domain.exception.UserNotFoundException;
import com.palvair.jwtauthentication.presentation.AuthenticationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthentificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthentificationService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenHelper jwtTokenHelper;

    @Autowired
    public JwtAuthentificationService(final AuthenticationManager authenticationManager,
                                      final JwtTokenHelper jwtTokenHelper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    public JwtResponse authenticate(final AuthenticationCommand command) throws Exception {
        final UserDetails userDetails = authenticateInternal(command);
        final String token = jwtTokenHelper.generateToken(userDetails);
        LOGGER.debug("Generated token = [{}]", token);
        return new JwtResponse(token);
    }

    private UserDetails authenticateInternal(final AuthenticationCommand command) throws Exception {
        try {
            LOGGER.debug("Authenticate with command [{}]", command);
            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(command.getEmail(), command.getPassword()));
            LOGGER.debug("Authentication successful [{}]", authentication);
            return (UserDetails) authentication.getPrincipal();
        } catch (final DisabledException exception) {
            throw new Exception("USER_DISABLED", exception);
        } catch (final BadCredentialsException exception) {
            throw new Exception("INVALID_CREDENTIALS", exception);
        } catch (final AuthenticationException exception) {
            final Throwable cause = exception.getCause();
            if (cause instanceof UserNotFoundException) {
                throw (UserNotFoundException) cause;
            }
            throw exception;
        }
    }
}
