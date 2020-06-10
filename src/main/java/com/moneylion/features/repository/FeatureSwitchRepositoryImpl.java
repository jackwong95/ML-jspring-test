package com.moneylion.features.repository;

import com.moneylion.features.dao.FeatureDAO;
import com.moneylion.features.dao.UserDAO;
import com.moneylion.features.dao.UserFeatureSwitchDAO;
import com.moneylion.features.exception.InvalidFeatureException;
import com.moneylion.features.exception.InvalidUserException;
import com.moneylion.features.exception.InvalidUserFeatureSwitchException;
import com.moneylion.features.model.Feature;
import com.moneylion.features.model.User;
import com.moneylion.features.model.UserFeatureEmbeddedId;
import com.moneylion.features.model.UserFeatureSwitch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class FeatureSwitchRepositoryImpl implements FeatureSwitchRepository {

    @Autowired
    private FeatureDAO featureDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserFeatureSwitchDAO userFeatureSwitchDAO;

    public void setFeatureDAO (FeatureDAO featureDAO) { this.featureDAO = featureDAO; }
    public void setUserDAO (UserDAO userDAO) { this.userDAO = userDAO; }
    public void setUserFeatureSwitchDAO (UserFeatureSwitchDAO userFeatureSwitchDAO) { this.userFeatureSwitchDAO = userFeatureSwitchDAO; }
    
    @Override
    public boolean canUserAccessFeature(String email, String featureName) {
        User user = null;
        Feature feature = null;
        // TODO: Exception handling here
        try {
            user = userDAO.findByEmail(email);
            feature = featureDAO.findByName(featureName);
            return userFeatureSwitchDAO.findSwitchById(new UserFeatureEmbeddedId(user.getId(), feature.getId())).isEnabled();
        } catch (EmptyResultDataAccessException e) {
            if (email == null || user == null) throw new InvalidUserException("Unable to retrieve user");
            if (featureName == null || feature == null) throw new InvalidFeatureException("Unable to retrieve feature");
        }

        return false;
    }

    @Transactional
    public void upsertFeature(String email, String featureName, Boolean enabled) {
        // TODO: Exception handling here
        User user = upsertUser(email);
        Feature feature = upsertFeature(featureName);
        upsertUserFeatureSwitch (user, feature, enabled);
    }

    private UserFeatureSwitch upsertUserFeatureSwitch (User user, Feature feature, Boolean enabled) {
        UserFeatureSwitch userFeatureSwitch;
        UserFeatureEmbeddedId embeddedId = new UserFeatureEmbeddedId(user.getId(), feature.getId());

        // try to find user feature switch
        try {
            userFeatureSwitch = userFeatureSwitchDAO.findSwitchById(embeddedId);

            // update switch status
            userFeatureSwitch.setEnabled(enabled);
            userFeatureSwitchDAO.updateUserFeatureSwitch(userFeatureSwitch);

        // userFeatureSwitch doesn't exist, then create a user feature switch and insert into database
        } catch (EmptyResultDataAccessException e) {
            userFeatureSwitch = new UserFeatureSwitch(embeddedId, enabled);
            userFeatureSwitchDAO.insertUserFeatureSwitch(userFeatureSwitch);
        }

        return userFeatureSwitchDAO.findSwitchById(embeddedId);
    }

    private User upsertUser (String email) {

        // try to return user
        try {
            return userDAO.findByEmail(email);
        // user does not exist, create a new user instance
        } catch (EmptyResultDataAccessException e) {
            userDAO.insertUser(new User(email));
        }

        return userDAO.findByEmail(email);
    }

    private Feature upsertFeature (String featureName) {

        // try to return feature
        try {
            return featureDAO.findByName(featureName);
        // feature does not exist, create a new feature instance
        } catch (EmptyResultDataAccessException e) {
            featureDAO.insertFeature(new Feature(featureName));
        }

        return featureDAO.findByName(featureName);
    }


}
