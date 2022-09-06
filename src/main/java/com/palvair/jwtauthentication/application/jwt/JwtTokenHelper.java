package com.palvair.jwtauthentication.application.jwt;

import com.palvair.jwtauthentication.domain.User;
import com.palvair.jwtauthentication.domain.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//FIXME: faire tests unitaires
//FIXME: factory pour creation des composants JWT
@Component
public class JwtTokenHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenHelper.class);
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 1000;

    private final Key key;
    private final UserRepository userRepository;

    public JwtTokenHelper(final @Value("${jwt.secret}") String secret, final UserRepository userRepository) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.userRepository = userRepository;
    }

    public String getUsernameFromToken(final String token) {
        try {
            return getClaimFromToken(token, Claims::getSubject);
        } catch (final JwtException | IllegalArgumentException exception) {
            LOGGER.error(exception.getMessage());
        }
        return null;
    }

    public String generateToken(final UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public Authentication validateToken(final String token) {
        if(!isTokenExpired(token)) {
            final String username = getUsernameFromToken(token);
            final User user = userRepository.getByUserName(username);
            return new UsernamePasswordAuthenticationToken(user, null, null);
        }
        return null;
    }

    private Date getExpirationDateFromToken(final String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(final String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String doGenerateToken(final Map<String, Object> claims, final String subject) {
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }
}