package com.moneylion.features.repository;

import com.moneylion.features.dao.*;
import com.moneylion.features.exception.InvalidFeatureException;
import com.moneylion.features.exception.InvalidUserException;
import com.moneylion.features.exception.InvalidUserFeatureSwitchException;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

public class FeatureSwitchRepositoryUnitTest {

    @Test
    public void featureSwitchRepositoryUnitTests () {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/schema.sql")
                .build();

        // initialize DAOs
        UserFeatureSwitchDAO userFeatureSwitchDAO = new UserFeatureSwitchDAOImpl();
        userFeatureSwitchDAO.setDataSource(dataSource);

        UserDAO userDAO = new UserDAOImpl();
        userDAO.setDataSource(dataSource);

        FeatureDAO featureDAO = new FeatureDAOImpl();
        featureDAO.setDataSource(dataSource);

        // initialize repo
        FeatureSwitchRepositoryImpl repo = new FeatureSwitchRepositoryImpl();

        // setting DAOs
        repo.setFeatureDAO(featureDAO);
        repo.setUserDAO(userDAO);
        repo.setUserFeatureSwitchDAO(userFeatureSwitchDAO);

        repo.upsertFeature("e1", "f1", true);
        repo.upsertFeature("e1", "f2", false);
        repo.upsertFeature("e1", "f3", true);

        repo.upsertFeature("e2", "f2", true);
        repo.upsertFeature("e2", "f3", true);
        repo.upsertFeature("e2", "f4", false);

        // try invalid upserts
        assertThrows(InvalidUserException.class, () ->
                repo.upsertFeature(null, "f2", true));
        assertThrows(InvalidFeatureException.class, () ->
                repo.upsertFeature("e2", null, true));
        assertThrows(InvalidUserFeatureSwitchException.class, () ->
                repo.upsertFeature("e2", "f2", null));

        // all the features should match with the above
        assertTrue(repo.canUserAccessFeature("e1", "f1"));
        assertFalse(repo.canUserAccessFeature("e1", "f2"));
        assertTrue(repo.canUserAccessFeature("e1", "f3"));

        assertTrue(repo.canUserAccessFeature("e2", "f2"));
        assertTrue(repo.canUserAccessFeature("e2", "f3"));
        assertFalse(repo.canUserAccessFeature("e2", "f4"));

        // try to flip all the features
        repo.upsertFeature("e1", "f1", false);
        repo.upsertFeature("e1", "f2", true);
        repo.upsertFeature("e1", "f3", false);

        repo.upsertFeature("e2", "f2", false);
        repo.upsertFeature("e2", "f3", false);
        repo.upsertFeature("e2", "f4", true);

        // all the features should match with the above
        assertFalse(repo.canUserAccessFeature("e1", "f1"));
        assertTrue(repo.canUserAccessFeature("e1", "f2"));
        assertFalse(repo.canUserAccessFeature("e1", "f3"));

        assertFalse(repo.canUserAccessFeature("e2", "f2"));
        assertFalse(repo.canUserAccessFeature("e2", "f3"));
        assertTrue(repo.canUserAccessFeature("e2", "f4"));


        // let's try some invalid requests
        assertThrows(InvalidUserException.class, () ->
                repo.canUserAccessFeature(null, "f2"));
        assertThrows(InvalidFeatureException.class, () ->
                repo.canUserAccessFeature("e2", null));

        // try valid request but user/feature doesnt exist
        assertThrows(InvalidFeatureException.class, () ->
                repo.canUserAccessFeature("e2", "invalid feature"));
        assertThrows(InvalidUserException.class, () ->
                repo.canUserAccessFeature("invalid user", "f2"));
        assertThrows(InvalidUserException.class, () ->
                repo.canUserAccessFeature("invalid user", "invalid feature"));

    }

}
