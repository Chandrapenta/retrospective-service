package com.assignment.retrospectiveservice.dto;

import com.assignment.retrospectiveservice.model.FeedbackType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for FeedbackItem.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackItemDto {
    @NotBlank(message = "Name of the person providing feedback is required")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @Size(min = 1, max = 1000, message = "Body length must not exceed 1000 characters")
    private String body;

    @NotNull(message = "Feedback type is required")
    private FeedbackType feedbackType;

}
