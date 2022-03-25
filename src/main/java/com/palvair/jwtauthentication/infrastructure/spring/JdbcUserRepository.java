package com.palvair.jwtauthentication.infrastructure.spring;

import com.palvair.jwtauthentication.domain.User;
import com.palvair.jwtauthentication.domain.UserRepository;
import com.palvair.jwtauthentication.domain.exception.AuthentificationException;
import com.palvair.jwtauthentication.domain.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUserRepository.class);
    private final NamedParameterJdbcOperations jdbcOperations;

    @Autowired
    public JdbcUserRepository(final NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public User getByEmailAndPassword(String email, String password) {
        final String sql = "SELECT users.nom, " +
                " users.prenom " +
                " FROM users " +
                " WHERE users.email = :email " +
                " AND users.password = :password";
        final SqlParameterSource sqlParameterSource = new MapSqlParameterSource("email", email)
                .addValue("password", password);
        try {
            return jdbcOperations.queryForObject(sql, sqlParameterSource, this::mapUser);
        } catch (final EmptyResultDataAccessException erdaex) {
            final String message = String.format("Aucun utilisateur trouvé avec l'email %s", email);
            LOGGER.warn(message, erdaex);
            throw new UserNotFoundException(message, erdaex);
        } catch (final DataAccessException daex) {
            final String message = String.format("Erreur pendant la récupération de l'utilisateur avec l'email %s", email);
            LOGGER.warn(message, daex);
            throw new AuthentificationException(message, daex);
        }
    }

    @Override
    public User getByUserName(String email) {
        final String sql = "SELECT users.nom, " +
                " users.prenom, " +
                " users.password, " +
                " users.email " +
                " FROM users " +
                " WHERE users.email = :email ";
        final SqlParameterSource sqlParameterSource = new MapSqlParameterSource("email", email);
        try {
            return jdbcOperations.queryForObject(sql, sqlParameterSource, this::mapUser);
        } catch (final EmptyResultDataAccessException erdaex) {
            final String message = String.format("Aucun utilisateur trouvé avec l'email %s", email);
            LOGGER.warn(message, erdaex);
            throw new UserNotFoundException(message, erdaex);
        } catch (final DataAccessException daex) {
            final String message = String.format("Erreur pendant la récupération de l'utilisateur avec l'email %s", email);
            LOGGER.warn(message, daex);
            throw new AuthentificationException(message, daex);
        }
    }

    private User mapUser(final ResultSet resultSet, int i) throws SQLException {
        return new User(resultSet.getString("nom"), resultSet.getString("prenom"), resultSet.getString("password"), resultSet.getString("email"));
    }
}
