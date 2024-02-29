package com.assignment.retrospectiveservice.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for Retrospective.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrospectiveDto {
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 255, message = "Name length must be between 1 and 255 characters")
    private String name;

    @Size(max = 1000, message = "Summary length must not exceed 1000 characters")
    private String summary;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Participants list is required")
    private List<String> participants;

    @Valid
    private List<FeedbackItemDto> feedbackItems;
}
