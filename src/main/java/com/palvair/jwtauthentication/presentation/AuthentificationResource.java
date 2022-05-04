package com.palvair.jwtauthentication.presentation;

import com.palvair.jwtauthentication.application.JwtAuthentificationService;
import com.palvair.jwtauthentication.domain.exception.UserNotFoundException;
import com.palvair.jwtauthentication.presentation.error.AuthenticationBadRequest;
import com.palvair.jwtauthentication.presentation.error.AuthentificationServerError;
import com.palvair.jwtauthentication.presentation.error.UserNotFound;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthentificationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthentificationResource.class);

    @Autowired
    private JwtAuthentificationService jwtAuthentificationService;

    @PostMapping("/authentifier")
    public ResponseEntity<?> authentifier(@RequestBody final AuthenticationCommand authenticationCommand) {
        LOGGER.debug("Request to authenticate [{}]", authenticationCommand);
        final String email = authenticationCommand.getEmail();
        if (StringUtils.isBlank(email)) {
            return new ResponseEntity<>(new AuthenticationBadRequest("Email is mandatory"),
                    HttpStatus.BAD_REQUEST);
        }
        final String password = authenticationCommand.getPassword();
        if (StringUtils.isBlank(password)) {
            return new ResponseEntity<>(new AuthenticationBadRequest("Password is mandatory"),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            return ResponseEntity.ok(
                    jwtAuthentificationService.authenticate(authenticationCommand)
            );
        } catch (final UserNotFoundException ex) {
            LOGGER.warn("User not found", ex);
            return new ResponseEntity<>(new UserNotFound("No user found"),
                    HttpStatus.NOT_FOUND);
            //FIXME: affiner les exceptions
        } catch (final Exception exception) {
            LOGGER.error("Errors while trying to authenticate user", exception);
            return new ResponseEntity<>(new AuthentificationServerError(exception.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
