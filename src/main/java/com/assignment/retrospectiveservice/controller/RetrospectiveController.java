package com.assignment.retrospectiveservice.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.retrospectiveservice.dto.FeedbackItemDto;
import com.assignment.retrospectiveservice.dto.RetrospectiveDto;
import com.assignment.retrospectiveservice.service.RetrospectiveService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/retrospectives", produces = { MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE })
@Slf4j
@AllArgsConstructor
public class RetrospectiveController {

        private RetrospectiveService retrospectiveService;

        // Endpoint to create a retrospective
        @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
        public ResponseEntity<RetrospectiveDto> createRetrospective(
                        @Valid @RequestBody RetrospectiveDto retrospectiveDto) {
                log.info("Creating retrospective: {}", retrospectiveDto);
                RetrospectiveDto createdRetrospectiveDto = retrospectiveService.createRetrospective(retrospectiveDto);
                log.info("Retrospective created: {}", createdRetrospectiveDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdRetrospectiveDto);
        }

        // Endpoint to add feedback item to a retrospective
        @PostMapping(path = "/{retrospectiveName}/feedback", consumes = { MediaType.APPLICATION_JSON_VALUE,
                        MediaType.APPLICATION_XML_VALUE })
        public ResponseEntity<RetrospectiveDto> addFeedbackItem(@PathVariable String retrospectiveName,
                        @Valid @RequestBody FeedbackItemDto feedbackItemDto) {
                log.info("Adding feedback item to retrospective '{}': {}", retrospectiveName, feedbackItemDto);
                RetrospectiveDto updatedRetrospectiveDto = retrospectiveService.addFeedbackItem(retrospectiveName,
                                feedbackItemDto);
                log.info("Feedback item added to retrospective '{}': {}", retrospectiveName, updatedRetrospectiveDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(updatedRetrospectiveDto);
        }

        // Endpoint to update feedback item in a retrospective
        @PutMapping("/{retrospectiveName}/feedback/{feedbackItemId}")
        public ResponseEntity<RetrospectiveDto> updateFeedbackItem(@PathVariable String retrospectiveName,
                        @PathVariable String feedbackItemId, @Valid @RequestBody FeedbackItemDto feedbackItemDto) {
                log.info("Updating feedback item '{}' for retrospective '{}': {}", feedbackItemId, retrospectiveName,
                                feedbackItemDto);
                RetrospectiveDto updatedRetrospectiveDto = retrospectiveService.updateFeedbackItem(retrospectiveName,
                                feedbackItemId, feedbackItemDto);
                if (updatedRetrospectiveDto != null) {
                        log.info("Feedback item '{}' updated for retrospective '{}': {}", feedbackItemId,
                                        retrospectiveName,
                                        updatedRetrospectiveDto);
                        return ResponseEntity.ok(updatedRetrospectiveDto);
                } else {
                        log.error("Failed to update feedback item '{}' for retrospective '{}'", feedbackItemId,
                                        retrospectiveName);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
        }

        // Endpoint to get all retrospectives with pagination
        @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
        public ResponseEntity<Page<RetrospectiveDto>> getAllRetrospectives(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int pageSize) {
                log.debug("Fetching all retrospectives with pagination (page={}, pageSize={})", page, pageSize);
                Page<RetrospectiveDto> retrospectivesPage = retrospectiveService.getAllRetrospectives(page, pageSize);
                log.debug("Retrieved {} retrospectives", retrospectivesPage.getTotalElements());
                return ResponseEntity.ok(retrospectivesPage);
        }

        // Endpoint to search retrospectives by date
        @GetMapping(path = "/search", produces = { MediaType.APPLICATION_JSON_VALUE,
                        MediaType.APPLICATION_XML_VALUE })
        public ResponseEntity<Page<RetrospectiveDto>> searchRetrospectivesByDate(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
                log.info("Searching retrospectives by date (date={}, page={}, pageSize={})", date, page, pageSize);
                Page<RetrospectiveDto> retrospectivesPage = retrospectiveService.searchRetrospectivesByDate(date, page,
                                pageSize);
                log.info("Retrieved {} retrospectives for date {}", retrospectivesPage.getTotalElements(), date);
                return ResponseEntity.ok(retrospectivesPage);
        }

}
