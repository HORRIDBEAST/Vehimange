package com.example.vehicle_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT) // Sets HTTP status to 409 Conflict
public class DuplicateRegistrationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateRegistrationException(String message) {
        super(message);
    }
}
