package com.assignment.retrospectiveservice.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Entity class representing a retrospective.
 */
@Entity
@Data
public class Retrospective {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String name;

    private String summary;

    @NotNull
    private LocalDate date;

    @NotEmpty
    @ElementCollection
    private List<String> participants;

    @OneToMany(cascade = CascadeType.ALL)
    private List<FeedbackItem> feedbackItems;

    // to avoid null pointer exception
    public Retrospective() {
        this.feedbackItems = new ArrayList<>();
    }
}
