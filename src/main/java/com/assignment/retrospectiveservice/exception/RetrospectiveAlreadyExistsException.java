package com.assignment.retrospectiveservice.exception;

/**
 * Exception thrown when attempting to create a retrospective that already exists.
 */
public class RetrospectiveAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a RetrospectiveAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public RetrospectiveAlreadyExistsException(String message) {
        super(message);
    }
}
