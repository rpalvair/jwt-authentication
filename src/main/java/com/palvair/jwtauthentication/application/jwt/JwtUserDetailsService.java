package com.palvair.jwtauthentication.application.jwt;

import com.palvair.jwtauthentication.domain.User;
import com.palvair.jwtauthentication.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUserDetailsService.class);

    private final UserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        LOGGER.debug("Loading user [{}]", username);
        final User user = userRepository.getByUserName(username);
        LOGGER.debug("User loaded = [{}]", user);
        return new User(user.getNom(), user.getPrenom(), user.getPassword(), user.getUsername());
    }
}
