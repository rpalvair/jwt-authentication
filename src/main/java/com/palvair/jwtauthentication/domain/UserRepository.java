package com.palvair.jwtauthentication.domain;


public interface UserRepository {

    User getByEmailAndPassword(final String email, final String password);

    User getByUserName(final String email);
}
