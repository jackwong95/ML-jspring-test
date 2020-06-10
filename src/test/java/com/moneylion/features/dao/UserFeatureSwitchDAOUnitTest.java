package com.moneylion.features.dao;

import com.moneylion.features.exception.InvalidUserException;
import com.moneylion.features.exception.InvalidUserFeatureSwitchException;
import com.moneylion.features.model.Feature;
import com.moneylion.features.model.User;
import com.moneylion.features.model.UserFeatureEmbeddedId;
import com.moneylion.features.model.UserFeatureSwitch;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserFeatureSwitchDAOUnitTest {

    @Test
    public void featureDAOTests () {
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

        // insert some users
        User u1 = new User("e1");
        User u2 = new User("e2");
        userDAO.insertUser(u1);
        userDAO.insertUser(u2);

        // insert some features
        Feature f1 = new Feature("f1");
        Feature f2 = new Feature("f2");
        featureDAO.insertFeature(f1);
        featureDAO.insertFeature(f2);

        // unit tests for userFeatureSwitchDAO

        // user id 1 have a feature 2 enabled
        UserFeatureEmbeddedId ufei1 = new UserFeatureEmbeddedId(1, 2);
        UserFeatureSwitch ufs1 = new UserFeatureSwitch(ufei1, true);

        // user id 2 have a feature 2 disabled
        UserFeatureEmbeddedId ufei2 = new UserFeatureEmbeddedId(2, 2);
        UserFeatureSwitch ufs2 = new UserFeatureSwitch(ufei2, false);

        // user id 1 have a feature 1 disabled
        UserFeatureEmbeddedId ufei3 = new UserFeatureEmbeddedId(1, 1);
        UserFeatureSwitch ufs3 = new UserFeatureSwitch(ufei3, false);

        // user id 2 have a feature 1 disabled
        UserFeatureEmbeddedId ufei4 = new UserFeatureEmbeddedId(2, 1);
        UserFeatureSwitch ufs4 = new UserFeatureSwitch(ufei4, false);

        // add all of these switches to the featureSwitchDAO
        userFeatureSwitchDAO.insertUserFeatureSwitch(ufs1);
        userFeatureSwitchDAO.insertUserFeatureSwitch(ufs2);
        userFeatureSwitchDAO.insertUserFeatureSwitch(ufs3);
        userFeatureSwitchDAO.insertUserFeatureSwitch(ufs4);

        // testing all the rows has been inserted
        assertEquals("UserFeatureSwitch{id=CompositeKey {userId=1, featureId='2'}, enabled='true'}", userFeatureSwitchDAO.findSwitchById(ufei1).toString());
        assertEquals("UserFeatureSwitch{id=CompositeKey {userId=2, featureId='2'}, enabled='false'}", userFeatureSwitchDAO.findSwitchById(ufei2).toString());
        assertEquals("UserFeatureSwitch{id=CompositeKey {userId=1, featureId='1'}, enabled='false'}", userFeatureSwitchDAO.findSwitchById(ufei3).toString());
        assertEquals("UserFeatureSwitch{id=CompositeKey {userId=2, featureId='1'}, enabled='false'}", userFeatureSwitchDAO.findSwitchById(ufei4).toString());

        // try to insert something that isn't valid
        UserFeatureEmbeddedId ufei_invalid = new UserFeatureEmbeddedId();
        UserFeatureSwitch ufs_invalid = new UserFeatureSwitch(ufei_invalid, false);
        assertThrows(DataIntegrityViolationException.class, () ->
                userFeatureSwitchDAO.insertUserFeatureSwitch(ufs_invalid));

        // set an id that doesn't exist in the database
        UserFeatureEmbeddedId ufei_invalid2 = new UserFeatureEmbeddedId(10, 10);
        UserFeatureSwitch ufs_invalid2 = new UserFeatureSwitch(ufei_invalid2, false);
        assertThrows(DataIntegrityViolationException.class, () ->
                userFeatureSwitchDAO.insertUserFeatureSwitch(ufs_invalid2));

        // try to update a switch
        ufs4.setEnabled(true);
        userFeatureSwitchDAO.updateUserFeatureSwitch(ufs4);
        assertEquals("UserFeatureSwitch{id=CompositeKey {userId=2, featureId='1'}, enabled='true'}", userFeatureSwitchDAO.findSwitchById(ufei4).toString());

        // try to set an invalid switch
        ufs4.setEnabled(null);
        InvalidUserFeatureSwitchException invalidUserFeatureSwitchException = assertThrows(InvalidUserFeatureSwitchException.class, () ->
                userFeatureSwitchDAO.updateUserFeatureSwitch(ufs4));
        ufs4.setEnabled(true);


        // try to delete a switch
        userFeatureSwitchDAO.deleteUserFeatureSwitch(ufs4);

        // switch has been deleted, therefore will throw an error complaining that the result is empty
        assertThrows(EmptyResultDataAccessException.class, () ->
                userFeatureSwitchDAO.findSwitchById(ufei4));

        // try to delete an invalid switch
        ufs4.setEnabled(null);
        assertThrows(InvalidUserFeatureSwitchException.class, () ->
                userFeatureSwitchDAO.deleteUserFeatureSwitch(ufs4));

        // get a list of switches that a user currently have
        ArrayList<UserFeatureSwitch> userFeatureList = Lists.newArrayList(userFeatureSwitchDAO.findSwitchByEmailName("e1"));
        assertEquals("UserFeatureSwitch{id=CompositeKey {userId=1, featureId='2'}, enabled='true'}", userFeatureList.get(0).toString());
        assertEquals("UserFeatureSwitch{id=CompositeKey {userId=1, featureId='1'}, enabled='false'}", userFeatureList.get(1).toString());

        userFeatureList = Lists.newArrayList(userFeatureSwitchDAO.findSwitchByEmailName("e2"));
        assertEquals("UserFeatureSwitch{id=CompositeKey {userId=2, featureId='2'}, enabled='false'}", userFeatureList.get(0).toString());

        // get a list of switches from feature name
        userFeatureList = Lists.newArrayList(userFeatureSwitchDAO.findSwitchByFeatureName("f2"));
        assertEquals("UserFeatureSwitch{id=CompositeKey {userId=1, featureId='2'}, enabled='true'}", userFeatureList.get(0).toString());
        assertEquals("UserFeatureSwitch{id=CompositeKey {userId=2, featureId='2'}, enabled='false'}", userFeatureList.get(1).toString());

        userFeatureList = Lists.newArrayList(userFeatureSwitchDAO.findSwitchByFeatureName("f1"));
        assertEquals("UserFeatureSwitch{id=CompositeKey {userId=1, featureId='1'}, enabled='false'}", userFeatureList.get(0).toString());

    }

}
