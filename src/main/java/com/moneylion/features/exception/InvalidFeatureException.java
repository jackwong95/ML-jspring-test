package com.moneylion.features.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class InvalidFeatureException extends DataIntegrityViolationException {
    public InvalidFeatureException(String errorMessage){
        super(errorMessage);
    }
}
