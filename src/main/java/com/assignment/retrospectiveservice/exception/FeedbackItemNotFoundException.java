package com.assignment.retrospectiveservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a feedback item is not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FeedbackItemNotFoundException extends RuntimeException {

    /**
     * Constructs a FeedbackItemNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public FeedbackItemNotFoundException(String message) {
        super(message);
    }
}
