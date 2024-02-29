package com.assignment.retrospectiveservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a retrospective is not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RetrospectiveNotFoundException extends RuntimeException {

    /**
     * Constructs a RetrospectiveNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public RetrospectiveNotFoundException(String message) {
        super(message);
    }
}
