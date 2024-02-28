package com.assignment.retrospectiveservice.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.retrospectiveservice.model.FeedbackItem;
import com.assignment.retrospectiveservice.model.Retrospective;
import com.assignment.retrospectiveservice.service.RetrospectiveService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/retrospectives", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Slf4j
@AllArgsConstructor
public class RetrospectiveController {

    private RetrospectiveService retrospectiveService;

@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Retrospective> createRetrospective(@RequestBody Retrospective retrospective) {
        log.info("Creating retrospective: {}", retrospective);
        // Sample implementation to create a new retrospective
        Retrospective createdRetrospective = retrospectiveService.createRetrospective(retrospective);
        log.info("Retrospective created: {}", createdRetrospective);
        return ResponseEntity.ok(createdRetrospective); 
    }

    @PostMapping("/{retrospectiveName}/feedback")
    public ResponseEntity<Retrospective> addFeedbackItem(@PathVariable String retrospectiveName, @RequestBody FeedbackItem feedbackItem) {
        log.info("Adding feedback item to retrospective: {}", retrospectiveName);
        // Sample implementation to add feedback item to a retrospective
        Retrospective updatedRetrospective = retrospectiveService.addFeedbackItem(retrospectiveName, feedbackItem);
        log.info("Feedback item added to retrospective: {}", updatedRetrospective);
        return ResponseEntity.ok(updatedRetrospective);
    }

    @PutMapping("/{retrospectiveName}/feedback/{feedbackItemId}")
    public ResponseEntity<Retrospective> updateFeedbackItem(@PathVariable String retrospectiveName, @PathVariable String feedbackItemId, @RequestBody FeedbackItem feedbackItem) {
        log.info("Updating feedback item {} for retrospective: {}", feedbackItemId, retrospectiveName);
        // Sample implementation to update feedback item
        Retrospective updatedRetrospective = retrospectiveService.updateFeedbackItem(retrospectiveName, feedbackItemId, feedbackItem);
        log.info("Feedback item {} updated for retrospective: {}", feedbackItemId, updatedRetrospective);
        return ResponseEntity.ok(updatedRetrospective);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Retrospective>> getAllRetrospectives(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        log.info("Fetching all retrospectives with pagination (page={}, pageSize={})", page, pageSize);
        // Sample implementation to return all retrospectives with pagination
        List<Retrospective> retrospectives = retrospectiveService.getAllRetrospectives(page, pageSize);
        log.info("Retrieved {} retrospectives", retrospectives.size());
        return ResponseEntity.ok(retrospectives);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Retrospective>> searchRetrospectivesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        log.info("Searching retrospectives by date (date={}, page={}, pageSize={})", date, page, pageSize);
        // Sample implementation to search retrospectives by date with pagination
        List<Retrospective> retrospectives = retrospectiveService.searchRetrospectivesByDate(date, page, pageSize);
        log.info("Retrieved {} retrospectives for date {}", retrospectives.size(), date);
        return ResponseEntity.ok(retrospectives);
    }
    
}
