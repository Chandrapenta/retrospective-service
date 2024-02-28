package com.assignment.retrospectiveservice.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class Retrospective {
    private String name;
    private String summary;
    private LocalDate date;
    private List<String> participants;
    private List<FeedbackItem> feedbackItems;
}
