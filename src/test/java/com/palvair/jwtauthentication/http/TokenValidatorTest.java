package com.palvair.jwtauthentication.http;


import com.palvair.jwtauthentication.application.jwt.JwtTokenHelper;
import com.palvair.jwtauthentication.domain.User;
import com.palvair.jwtauthentication.infrastructure.http.TokenValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenValidatorTest {

    private static final String USERNAME = "r.palvair@gmail.com";
    private static final String TOKEN = "token";

    @InjectMocks
    private TokenValidator tokenValidator;
    @Mock
    private JwtTokenHelper jwtTokenHelper;

    @AfterEach
    public void clean() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void should_return_user_details_when_token_valide() {
        final User user = new User("Palvair", "Ruddy", "motdepssse", USERNAME);
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(user, null));

        when(jwtTokenHelper.validateToken(TOKEN, user)).thenReturn(true);

        final UserDetails userDetails = tokenValidator.validateToken(TOKEN);

        assertThat(userDetails).isNotNull()
                .extracting(UserDetails::getUsername)
                .isEqualTo(USERNAME);
    }

    @Test
    public void should_return_false_when_token_invalide() {
        final User user = new User("Palvair", "Ruddy", "motdepssse", USERNAME);
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(user, null));

        when(jwtTokenHelper.validateToken(TOKEN, user)).thenReturn(false);

        final UserDetails userDetails = tokenValidator.validateToken(TOKEN);

        assertThat(userDetails).isNull();
    }

    @Test
    void should_return_null_when_username_not_found_in_token() {
        when(jwtTokenHelper.validateToken(TOKEN, null)).thenReturn(false);

        final UserDetails userDetails = tokenValidator.validateToken(TOKEN);

        assertThat(userDetails).isNull();
    }
}