package com.moneylion.features.dao;

import com.moneylion.features.exception.InvalidFeatureException;
import com.moneylion.features.model.Feature;
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
public class FeatureDAOImpl implements FeatureDAO {

    @Autowired
    private JdbcTemplate template;

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public void setDataSource(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Feature> findAll() {
        String sql = "SELECT * FROM Feature";
        return template.query(sql, (rs, row) -> {
            Feature f = new Feature();
            f.setId(rs.getInt(1));
            f.setName(rs.getString(2));
            return f;
        });
    }

    @Override
    public Feature findById(Integer id) throws EmptyResultDataAccessException {
        String sql="SELECT f_id, name FROM Feature WHERE f_id = ?";
        Map<String,Object> result = template.queryForMap(sql, id);
        return new Feature((int)result.get("F_ID"), (String)result.get("NAME"));
    }

    @Override
    public Feature findByName(String name) throws EmptyResultDataAccessException {
        String sql="SELECT f_id, name FROM Feature WHERE name = ?";
        Map<String,Object> result = template.queryForMap(sql, name);
        return new Feature((int)result.get("F_ID"), (String)result.get("NAME"));
    }

    @Override
    public List<Feature> findByUserId(Integer userId) {
        String sql = "SELECT f.* FROM UserFeature uf JOIN User u ON uf.u_id = u.u_id JOIN Feature f on uf.f_id = f.f_id WHERE uf.u_id = ?";
        return template.query(sql,
            new Object[]{userId},
                (rs, row) -> {
                    Feature f=new Feature();
                    f.setId(rs.getInt(1));
                    f.setName(rs.getString(2));
                    return f;
                });
    }

    @Override
    public List<Feature> findByUserEmail(String email) {
        String sql = "SELECT f.* FROM UserFeature uf JOIN User u ON uf.u_id = u.u_id JOIN Feature f on uf.f_id = f.f_id WHERE u.email = ?";
        return template.query(sql,
                new Object[]{email},
                (rs, row) -> {
                    Feature f=new Feature();
                    f.setId(rs.getInt(1));
                    f.setName(rs.getString(2));
                    return f;
                });
    }

    @Transactional
    public void insertFeature(Feature feature) throws InvalidFeatureException, DuplicateKeyException {
        validateFeature(feature);
        String sql = "INSERT INTO Feature (name) values (?)";
        template.update(sql, feature.getName());
    }

    private void validateFeature(Feature feature) {
        if (feature == null)
            throw new NullPointerException("Null feature cannot be inserted");
        if (feature.getName() == null)
            throw new InvalidFeatureException("Invalid feature exception: " + feature.toString());
    }

}
