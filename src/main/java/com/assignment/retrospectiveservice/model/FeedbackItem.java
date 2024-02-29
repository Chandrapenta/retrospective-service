package com.assignment.retrospectiveservice.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Entity class representing a feedback item.
 */
@Entity
@Data
public class FeedbackItem {

    /**
     * The unique identifier for the feedback item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The name of the person providing feedback.
     */
    @NotBlank
    private String name;

    /**
     * The body of the feedback item.
     */
    private String body;

    /**
     * The type of feedback.
     */
    @NotNull
    private FeedbackType feedbackType;
}
