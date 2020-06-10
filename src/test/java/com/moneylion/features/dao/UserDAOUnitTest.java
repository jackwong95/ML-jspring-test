package com.moneylion.features.dao;

import com.moneylion.features.exception.InvalidUserException;
import com.moneylion.features.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDAOUnitTest {

    @Test
    public void userDAOTests () {

        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/schema.sql")
                .build();

        // initialize DAO
        UserDAO userDAO = new UserDAOImpl();
        userDAO.setDataSource(dataSource);

        // normal insertions
        User u1 = new User("e1");
        User u2 = new User("e2");
        userDAO.insertUser(u1);
        userDAO.insertUser(u2);

        assertEquals("e1", userDAO.findById(1).getEmail());
        assertEquals("e2", userDAO.findById(2).getEmail());

        assertEquals("e1", userDAO.findByEmail("e1").getEmail());
        assertEquals("e2", userDAO.findByEmail("e2").getEmail());

        // get an invalid id and name
        assertThrows(EmptyResultDataAccessException.class, () ->
                userDAO.findById(10000));
        assertThrows(EmptyResultDataAccessException.class, () ->
                userDAO.findByEmail("Invalid Email!"));

        // test for edge cases
        User uInvalid = new User(null);
        assertThrows(InvalidUserException.class, () ->
                userDAO.insertUser(uInvalid));

        assertThrows(DuplicateKeyException.class, () ->
                userDAO.insertUser(u1));
        User uNull = null;
        assertThrows(NullPointerException.class, () ->
                userDAO.insertUser(uNull));

    }

}
