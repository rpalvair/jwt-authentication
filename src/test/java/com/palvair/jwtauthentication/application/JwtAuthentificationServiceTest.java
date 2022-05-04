package com.palvair.jwtauthentication.application;

import com.palvair.jwtauthentication.application.jwt.JwtResponse;
import com.palvair.jwtauthentication.application.jwt.JwtTokenHelper;
import com.palvair.jwtauthentication.domain.User;
import com.palvair.jwtauthentication.domain.exception.UserNotFoundException;
import com.palvair.jwtauthentication.presentation.AuthenticationCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthentificationServiceTest {

    @InjectMocks
    private JwtAuthentificationService jwtAuthentificationService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenHelper jwtTokenHelper;

    @Test
    public void should_throw_exception_when_user_desactive() {
        final AuthenticationCommand command = new AuthenticationCommand();
        command.setEmail("email");
        command.setPassword("password");

        doThrow(new DisabledException("")).when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("email", "password"));

        assertThatThrownBy(() -> jwtAuthentificationService.authenticate(command)).isInstanceOf(Exception.class)
                .hasMessage("USER_DISABLED");
    }

    @Test
    public void should_throw_exception_when_credentials_invalides() {
        final AuthenticationCommand command = new AuthenticationCommand();
        command.setEmail("email");
        command.setPassword("password");

        doThrow(new BadCredentialsException("")).when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("email", "password"));

        assertThatThrownBy(() -> jwtAuthentificationService.authenticate(command)).isInstanceOf(Exception.class)
                .hasMessage("INVALID_CREDENTIALS");
    }

    @Test
    public void should_rethrow_exception_when_token_helper_throw_exception() {
        final AuthenticationCommand command = new AuthenticationCommand();
        command.setEmail("email");
        command.setPassword("password");
        final User user = new User("Palvair", "Ruddy", "motdepasse", "r.palvair@gmail.com");
        final Authentication authentication = new UsernamePasswordAuthenticationToken(user, "password");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        doThrow(new IllegalArgumentException("Pas de bol")).when(jwtTokenHelper).generateToken((UserDetails) authentication.getPrincipal());

        assertThatThrownBy(() -> jwtAuthentificationService.authenticate(command)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Pas de bol");
    }

    @Test
    public void should_return_jwt_response_when_user_bien_authentifie() throws Exception {
        final AuthenticationCommand command = new AuthenticationCommand();
        command.setEmail("email");
        command.setPassword("password");
        final User user = new User("Palvair", "Ruddy", "motdepasse", "r.palvair@gmail.com");
        final Authentication authentication = new UsernamePasswordAuthenticationToken(user, "password");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenHelper.generateToken((UserDetails) authentication.getPrincipal())).thenReturn("token");

        final JwtResponse jwtResponse = jwtAuthentificationService.authenticate(command);

        assertThat(jwtResponse).isNotNull()
                .extracting(JwtResponse::getToken)
                .isEqualTo("token");
    }

    @Test
    public void should_throw_cause_when_user_not_found_exception() {
        final AuthenticationCommand command = new AuthenticationCommand();
        command.setEmail("email");
        command.setPassword("password");

        doThrow(new InternalAuthenticationServiceException("arf", new UserNotFoundException("Pas de bol"))).when(authenticationManager).authenticate(any());

        assertThatThrownBy(() -> jwtAuthentificationService.authenticate(command)).isInstanceOf(UserNotFoundException.class)
                .hasMessage("Pas de bol");
    }
}