package com.assignment.retrospectiveservice.model;

import lombok.Data;

@Data
public class FeedbackItem {
    private String name;
    private String body;
    private FeedbackType feedbackType;
}
