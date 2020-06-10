package com.moneylion.features.dao;

import com.moneylion.features.exception.InvalidUserFeatureSwitchException;
import com.moneylion.features.model.UserFeatureEmbeddedId;
import com.moneylion.features.model.UserFeatureSwitch;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public interface UserFeatureSwitchDAO {

    List<UserFeatureSwitch> findAll();

    List<UserFeatureSwitch> findSwitchByFeatureName(String featureName);
    List<UserFeatureSwitch> findSwitchByEmailName(String email);

    UserFeatureSwitch findSwitchById(UserFeatureEmbeddedId embeddedId);

    void insertUserFeatureSwitch(UserFeatureSwitch userFeatureSwitch) throws DataIntegrityViolationException;
    void updateUserFeatureSwitch(UserFeatureSwitch userFeatureSwitch) throws InvalidUserFeatureSwitchException;
    void deleteUserFeatureSwitch(UserFeatureSwitch userFeatureSwitch) throws InvalidUserFeatureSwitchException;

    void setTemplate(JdbcTemplate template);
    void setDataSource(DataSource dataSource);
}
