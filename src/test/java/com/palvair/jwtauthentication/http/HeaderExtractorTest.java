package com.palvair.jwtauthentication.http;

import com.palvair.jwtauthentication.infrastructure.http.HeaderExtractor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HeaderExtractorTest {

    @InjectMocks
    private HeaderExtractor headerExtractor;
    @Mock
    private HttpServletRequest httpServletRequest;

    @Test
    public void should_return_authorization_token_when_present_dans_header() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");

        final String token = headerExtractor.getAuthorizationToken(httpServletRequest);

        assertThat(token).isNotNull()
                .isEqualTo("token");
    }

    @Test
    public void should_return_null_when_header_mauvais_format() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("token");

        final String token = headerExtractor.getAuthorizationToken(httpServletRequest);

        assertThat(token).isNull();
    }
}