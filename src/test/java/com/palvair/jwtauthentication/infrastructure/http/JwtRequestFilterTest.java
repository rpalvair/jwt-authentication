package com.palvair.jwtauthentication.infrastructure.http;

import com.palvair.jwtauthentication.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    private static final String TOKEN = "Token";
    private static final String USERNAME = "r.palvair@gmail.com";

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;
    @Mock
    private HeaderExtractor headerExtractor;
    @Mock
    private TokenValidator tokenValidator;

    @Test
    void should_set_security_context_when_token_is_valid() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final User user = new User(null, null, null, USERNAME);

        when(headerExtractor.getAuthorizationToken(request)).thenReturn(TOKEN);
        when(tokenValidator.validateToken(TOKEN))
                .thenReturn(user);

        jwtRequestFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain());

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull()
                .extracting(Authentication::getPrincipal)
                .isEqualTo(user);
    }

    @Test
    void should_not_set_security_context_when_token_is_invalid() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();

        when(headerExtractor.getAuthorizationToken(request)).thenReturn(TOKEN);
        when(tokenValidator.validateToken(TOKEN))
                .thenReturn(null);

        jwtRequestFilter.doFilterInternal(request, new MockHttpServletResponse(), new MockFilterChain());

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}