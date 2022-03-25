package com.palvair.jwtauthentication.http;


import com.palvair.jwtauthentication.application.jwt.JwtTokenHelper;
import com.palvair.jwtauthentication.application.jwt.JwtUserDetailsService;
import com.palvair.jwtauthentication.domain.User;
import com.palvair.jwtauthentication.infrastructure.http.TokenValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenValidatorTest {

    @InjectMocks
    private TokenValidator tokenValidator;
    @Mock
    private JwtUserDetailsService jwtUserDetailsService;
    @Mock
    private JwtTokenHelper jwtTokenHelper;

    @Test
    public void should_return_user_details_when_token_valide() {
        final User user = new User("Palvair", "Ruddy", "motdepssse", "r.palvair@gmail.com");

        when(jwtUserDetailsService.loadUserByUsername("r.palvair@gmail.com")).thenReturn(user);
        when(jwtTokenHelper.validateToken("token", user)).thenReturn(true);

        final UserDetails userDetails = tokenValidator.validateToken("token", "r.palvair@gmail.com");

        assertThat(userDetails).isNotNull()
                .extracting(UserDetails::getUsername)
                .isEqualTo("r.palvair@gmail.com");

    }

    @Test
    public void should_return_false_when_token_invalide() {
        final User user = new User("Palvair", "Ruddy", "motdepssse", "r.palvair@gmail.com");

        when(jwtUserDetailsService.loadUserByUsername("r.palvair@gmail.com")).thenReturn(user);
        when(jwtTokenHelper.validateToken("token", user)).thenReturn(false);

        final UserDetails userDetails = tokenValidator.validateToken("token", "r.palvair@gmail.com");

        assertThat(userDetails).isNull();

    }
}