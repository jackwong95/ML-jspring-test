package com.moneylion.features.dao;

import com.moneylion.features.exception.InvalidUserFeatureSwitchException;
import com.moneylion.features.model.UserFeatureEmbeddedId;
import com.moneylion.features.model.UserFeatureSwitch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class UserFeatureSwitchDAOImpl implements UserFeatureSwitchDAO {

    @Autowired
    private JdbcTemplate template;

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public void setDataSource(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<UserFeatureSwitch> findAll() {
        String sql = "SELECT u_id, f_id, enabled FROM UserFeatureSwitch";
        return template.query(sql, (rs, row) -> {
            UserFeatureSwitch userFeatureSwitch = new UserFeatureSwitch();
            userFeatureSwitch.setId(new UserFeatureEmbeddedId(rs.getInt(1), rs.getInt(2)));
            userFeatureSwitch.setEnabled(rs.getBoolean(3));
            return userFeatureSwitch;
        });
    }

    @Override
    public List<UserFeatureSwitch> findSwitchByFeatureName(String featureName) {
        String sql = "SELECT ufs.u_id, ufs.f_id, ufs.enabled FROM UserFeatureSwitch ufs JOIN Feature f on ufs.f_id = f.f_id WHERE f.name = ?";
        return template.query(sql,
                new Object[]{featureName},
                (rs, row) -> {
                    UserFeatureSwitch userFeatureSwitch = new UserFeatureSwitch();
                    userFeatureSwitch.setId(new UserFeatureEmbeddedId(rs.getInt(1), rs.getInt(2)));
                    userFeatureSwitch.setEnabled(rs.getBoolean(3));
                    return userFeatureSwitch;
                });
    }

    @Override
    public List<UserFeatureSwitch> findSwitchByEmailName(String email) {
        String sql = "SELECT ufs.u_id, ufs.f_id, ufs.enabled FROM UserFeatureSwitch ufs JOIN User u ON ufs.u_id = u.u_id WHERE u.email = ?";
        return template.query(sql,
                new Object[]{email},
                (rs, row) -> {
                    UserFeatureSwitch userFeatureSwitch = new UserFeatureSwitch();
                    userFeatureSwitch.setId(new UserFeatureEmbeddedId(rs.getInt(1), rs.getInt(2)));
                    userFeatureSwitch.setEnabled(rs.getBoolean(3));
                    return userFeatureSwitch;
                });
    }

    @Override
    public UserFeatureSwitch findSwitchById(UserFeatureEmbeddedId embeddedId) {
        String sql = "SELECT ufs.u_id, ufs.f_id, ufs.enabled FROM UserFeatureSwitch ufs WHERE ufs.u_id = ? AND ufs.f_id = ?";
        Map<String,Object> result = template.queryForMap(sql, embeddedId.getUserId(), embeddedId.getFeatureId());

        UserFeatureSwitch userFeatureSwitch = new UserFeatureSwitch();
        userFeatureSwitch.setId(new UserFeatureEmbeddedId((int)result.get("U_ID"), (int)result.get("F_ID")));
        userFeatureSwitch.setEnabled((boolean)result.get("ENABLED"));

        return userFeatureSwitch;
    }

    @Transactional
    public void insertUserFeatureSwitch(UserFeatureSwitch userFeatureSwitch) throws DataIntegrityViolationException {
        validateUserFeatureSwitch(userFeatureSwitch);
        String sql = "INSERT INTO UserFeatureSwitch (u_id, f_id, enabled) values (?, ?, ?)";

        UserFeatureEmbeddedId id = userFeatureSwitch.getId();
        template.update(sql, id.getUserId(), id.getFeatureId(), userFeatureSwitch.isEnabled());
    }

    @Transactional
    public void updateUserFeatureSwitch(UserFeatureSwitch userFeatureSwitch) throws InvalidUserFeatureSwitchException {
        validateUserFeatureSwitch(userFeatureSwitch);
        String sql = "UPDATE UserFeatureSwitch SET enabled = ? WHERE u_id = ? and f_id = ?";

        UserFeatureEmbeddedId id = userFeatureSwitch.getId();
        template.update(sql, userFeatureSwitch.isEnabled(), id.getUserId(), id.getFeatureId());
    }

    @Transactional
    public void deleteUserFeatureSwitch(UserFeatureSwitch userFeatureSwitch) throws InvalidUserFeatureSwitchException {
        validateUserFeatureSwitch(userFeatureSwitch);
        String sql = "DELETE FROM UserFeatureSwitch WHERE u_id = ? and f_id = ?";

        UserFeatureEmbeddedId id = userFeatureSwitch.getId();
        template.update(sql, id.getUserId(), id.getFeatureId());
    }

    private void validateUserFeatureSwitch(UserFeatureSwitch userFeatureSwitch) {
        if (userFeatureSwitch == null)
            throw new NullPointerException("Null feature cannot be inserted");

        UserFeatureEmbeddedId id = userFeatureSwitch.getId();
        if (id == null || id.getFeatureId() == null || id.getUserId() == null
                || userFeatureSwitch.isEnabled() == null)
            throw new InvalidUserFeatureSwitchException("Invalid feature exception: " + userFeatureSwitch.toString());
    }

}
