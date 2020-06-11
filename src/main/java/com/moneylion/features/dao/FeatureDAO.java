package com.moneylion.features.dao;

import com.moneylion.features.exception.InvalidFeatureException;
import com.moneylion.features.model.Feature;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public interface FeatureDAO {

    List<Feature> findAll();

    // a user may have multiple or more features
    List<Feature> findByUserId(Integer userId);

    List<Feature> findByUserEmail(String email);

    // a feature can only have one id and one name
    Feature findById(Integer id) throws EmptyResultDataAccessException;

    Feature findByName(String name) throws EmptyResultDataAccessException;

    void insertFeature(Feature feature) throws InvalidFeatureException, DuplicateKeyException;

    void setTemplate(JdbcTemplate template);

    void setDataSource(DataSource dataSource);

}
