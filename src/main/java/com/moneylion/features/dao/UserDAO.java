package com.moneylion.features.dao;

import com.moneylion.features.exception.InvalidUserException;
import com.moneylion.features.model.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public interface UserDAO {

    List<User> findAll();

    List<User> findByFeatureId(Integer featureId);

    List<User> findByFeatureName(String featureName);

    User findById(Integer id) throws EmptyResultDataAccessException;

    User findByEmail(String email) throws EmptyResultDataAccessException;

    // TODO: verify all the possible exceptions thrown
    void insertUser(User user) throws InvalidUserException, DuplicateKeyException;

    void setTemplate(JdbcTemplate template);

    void setDataSource(DataSource dataSource);

}
