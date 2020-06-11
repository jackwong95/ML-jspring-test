package com.moneylion.features.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class InvalidUserException extends DataIntegrityViolationException {
    public InvalidUserException(String errorMessage) {
        super(errorMessage);
    }
}
