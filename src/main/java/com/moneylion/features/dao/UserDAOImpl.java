package com.moneylion.features.dao;

import com.moneylion.features.exception.InvalidUserException;
import com.moneylion.features.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate template;

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public void setDataSource(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM User";
        return template.query(sql, (rs, row) -> {
            User u = new User();
            u.setId(rs.getInt(1));
            u.setEmail(rs.getString(2));
            return u;
        });
    }

    @Override
    public User findById(Integer id) throws EmptyResultDataAccessException {
        String sql="SELECT u_id, email FROM User WHERE u_id = ?";
        Map<String,Object> result = template.queryForMap(sql, id);
        return new User((int)result.get("U_ID"), (String)result.get("EMAIL"));
    }

    @Override
    public User findByEmail(String email) throws EmptyResultDataAccessException  {
        String sql="SELECT u_id, email FROM User WHERE email = ?";
        Map<String,Object> result = template.queryForMap(sql, email);
        return new User((int)result.get("U_ID"), email);
    }

    @Override
    public List<User> findByFeatureId(Integer featureId) {
        String sql = "SELECT u.* FROM UserUser uf JOIN User u ON uf.u_id = u.u_id JOIN User f on uf.f_id = f.f_id WHERE uf.f_id = ?";
        return template.query(sql,
                new Object[]{featureId},
                (rs, row) -> {
                    User u = new User();
                    u.setId(rs.getInt(1));
                    u.setEmail(rs.getString(2));
                    return u;
                });
    }

    @Override
    public List<User> findByFeatureName(String featureName) {
        String sql = "SELECT u.* FROM UserUser uf JOIN User u ON uf.u_id = u.u_id JOIN User f on uf.f_id = f.f_id WHERE f.name = ?";
        return template.query(sql,
                new Object[]{featureName},
                (rs, row) -> {
                    User u = new User();
                    u.setId(rs.getInt(1));
                    u.setEmail(rs.getString(2));
                    return u;
                });
    }

    @Transactional
    public void insertUser(User user) throws InvalidUserException, DuplicateKeyException {
        validateUser(user);
        String sql = "INSERT INTO User (email) values (?)";
        template.update(sql, user.getEmail());
    }

    private void validateUser(User user) {
        if (user == null)
            throw new NullPointerException("Null feature cannot be inserted");
        if (user.getEmail() == null)
            throw new InvalidUserException("Invalid feature exception: " + user.toString());
    }

}
