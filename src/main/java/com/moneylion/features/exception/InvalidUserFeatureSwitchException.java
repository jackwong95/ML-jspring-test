package com.moneylion.features.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class InvalidUserFeatureSwitchException extends DataIntegrityViolationException {
    public InvalidUserFeatureSwitchException(String errorMessage){
        super(errorMessage);
    }
}
