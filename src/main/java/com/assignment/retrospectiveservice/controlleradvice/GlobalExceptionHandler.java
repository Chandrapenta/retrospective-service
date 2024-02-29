package com.assignment.retrospectiveservice.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.assignment.retrospectiveservice.exception.RetrospectiveAlreadyExistsException;
import com.assignment.retrospectiveservice.exception.RetrospectiveNotFoundException;
import com.assignment.retrospectiveservice.exception.ErrorResponse;
import com.assignment.retrospectiveservice.exception.FeedbackItemNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ RetrospectiveAlreadyExistsException.class })
    public ResponseEntity<ErrorResponse> handleRetrospectiveAlreadyExistsException(
            RetrospectiveAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler({ FeedbackItemNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleFeedbackItemNotFoundException(FeedbackItemNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({ RetrospectiveNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleRetrospectiveNotFoundException(RetrospectiveNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
