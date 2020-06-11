package com.moneylion.features.dao;

import com.moneylion.features.exception.InvalidFeatureException;
import com.moneylion.features.model.Feature;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FeatureDAOUnitTest {

    @Test
    public void featureDAOTests() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/schema.sql")
                .build();

        // initialize DAO
        FeatureDAO featureDAO = new FeatureDAOImpl();
        featureDAO.setDataSource(dataSource);

        // normal insertions
        Feature f1 = new Feature("f1");
        Feature f2 = new Feature("f2");
        featureDAO.insertFeature(f1);
        featureDAO.insertFeature(f2);

        // try to retrieve feature by feature id
        assertEquals("f1", featureDAO.findById(1).getName());
        assertEquals("f2", featureDAO.findById(2).getName());

        assertEquals("f1", featureDAO.findByName("f1").getName());
        assertEquals("f2", featureDAO.findByName("f2").getName());

        // get an invalid id and name
        assertThrows(EmptyResultDataAccessException.class, () ->
                featureDAO.findById(10000));
        assertThrows(EmptyResultDataAccessException.class, () ->
                featureDAO.findByName("Invalid Feature!"));

        // test for edge cases
        Feature fInvalid = new Feature(null);
        assertThrows(InvalidFeatureException.class, () ->
                featureDAO.insertFeature(fInvalid));
        assertThrows(DuplicateKeyException.class, () ->
                featureDAO.insertFeature(f1));
        Feature fNull = null;
        assertThrows(NullPointerException.class, () ->
                featureDAO.insertFeature(fNull));

    }

}
