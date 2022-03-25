package com.palvair.jwtauthentication.presentation;

import com.palvair.jwtauthentication.domain.UserRepository;
import com.palvair.jwtauthentication.domain.exception.AuthentificationException;
import com.palvair.jwtauthentication.domain.exception.UserNotFoundException;
import com.palvair.jwtauthentication.presentation.error.AuthentificationServerError;
import com.palvair.jwtauthentication.presentation.error.UserNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/users")
public class UserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") final String email) {
        LOGGER.debug("Recuperation user avec email {}", email);
        try {
            return ResponseEntity.ok(
                    userRepository.getByUserName(email)
            );
        } catch (final UserNotFoundException exception) {
            final String message = String.format("Utilisateur %s introuvable", email);
            LOGGER.error(message, exception);
            return new ResponseEntity<>(new UserNotFound(message), HttpStatus.NOT_FOUND);
        } catch (final AuthentificationException exception) {
            final String message = String.format("Erreur lors de la recuperation de l'utilisateur %s", email);
            LOGGER.error(message, exception);
            return new ResponseEntity<>(new AuthentificationServerError(message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
