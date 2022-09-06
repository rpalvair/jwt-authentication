package com.palvair.jwtauthentication.application.jwt;

import com.palvair.jwtauthentication.domain.User;
import com.palvair.jwtauthentication.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void should_charger_user_par_email() {
        final User user = new User("brown", "bobby", "motdepasse", "b.brown@gmail.com");

        when(userRepository.getByUserName("b.brown@gmail.com")).thenReturn(user);

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("b.brown@gmail.com");

        assertThat(userDetails).isNotNull()
                .extracting(UserDetails::getUsername)
                .isEqualTo("b.brown@gmail.com");
    }
}