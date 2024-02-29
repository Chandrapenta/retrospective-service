package com.assignment.retrospectiveservice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.assignment.retrospectiveservice.dto.FeedbackItemDto;
import com.assignment.retrospectiveservice.dto.RetrospectiveDto;
import com.assignment.retrospectiveservice.exception.FeedbackItemNotFoundException;
import com.assignment.retrospectiveservice.exception.RetrospectiveAlreadyExistsException;
import com.assignment.retrospectiveservice.exception.RetrospectiveNotFoundException;
import com.assignment.retrospectiveservice.model.FeedbackItem;
import com.assignment.retrospectiveservice.model.Retrospective;
import com.assignment.retrospectiveservice.repository.RetrospectiveRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class RetrospectiveService {

    private final RetrospectiveRepository retrospectiveRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a new retrospective.
     *
     * @param retrospectiveDto The data for the new retrospective.
     * @return The created retrospective.
     * @throws RetrospectiveAlreadyExistsException If a retrospective with the same
     *                                             name already exists.
     */
    public RetrospectiveDto createRetrospective(RetrospectiveDto retrospectiveDto) {
        log.info("Creating retrospective: {}", retrospectiveDto);
        // Check if a retrospective with the same name already exists
        Optional<Retrospective> existingRetrospective = retrospectiveRepository.findByName(retrospectiveDto.getName());
        if (existingRetrospective.isPresent()) {
            throw new RetrospectiveAlreadyExistsException("Retrospective with the given name already exists.");
        }
        Retrospective retrospective = modelMapper.map(retrospectiveDto, Retrospective.class);
        retrospective = retrospectiveRepository.save(retrospective);
        log.debug("Retrospective created successfully");
        return modelMapper.map(retrospective, RetrospectiveDto.class);
    }

    /**
     * Adds a feedback item to a retrospective.
     *
     * @param retrospectiveName The name of the retrospective.
     * @param feedbackItemDto   The feedback item data.
     * @return The updated retrospective.
     * @throws RetrospectiveNotFoundException If the retrospective with the given
     *                                        name is not found.
     */
    public RetrospectiveDto addFeedbackItem(String retrospectiveName, FeedbackItemDto feedbackItemDto) {
        log.info("Adding feedback item to retrospective: {}", retrospectiveName);
        Optional<Retrospective> optionalRetrospective = retrospectiveRepository.findByName(retrospectiveName);
        if (optionalRetrospective.isPresent()) {
            Retrospective retrospective = optionalRetrospective.get();
            FeedbackItem feedbackItem = modelMapper.map(feedbackItemDto, FeedbackItem.class);
            retrospective.getFeedbackItems().add(feedbackItem);
            retrospective = retrospectiveRepository.save(retrospective);
            log.debug("Feedback item added successfully to retrospective: {}", retrospectiveName);
            return modelMapper.map(retrospective, RetrospectiveDto.class);
        } else {
            log.error("Retrospective not found: {}", retrospectiveName);
            throw new RetrospectiveNotFoundException("Retrospective not found: " + retrospectiveName);
        }
    }

    /**
     * Updates a feedback item in a retrospective.
     *
     * @param retrospectiveName The name of the retrospective.
     * @param feedbackItemId    The ID of the feedback item to update.
     * @param feedbackItemDto   The updated feedback item data.
     * @return The updated retrospective.
     * @throws RetrospectiveNotFoundException If the retrospective with the given
     *                                        name is not found.
     * @throws FeedbackItemNotFoundException  If the feedback item with the given ID
     *                                        is not found in the retrospective.
     */
    public RetrospectiveDto updateFeedbackItem(String retrospectiveName, String feedbackItemId,
            FeedbackItemDto feedbackItemDto) {
        log.info("Updating feedback item {} for retrospective: {}", feedbackItemId, retrospectiveName);
        Optional<Retrospective> optionalRetrospective = retrospectiveRepository.findByName(retrospectiveName);
        if (optionalRetrospective.isPresent()) {
            Retrospective retrospective = optionalRetrospective.get();
            List<FeedbackItem> feedbackItems = retrospective.getFeedbackItems();
            boolean found = false;
            for (FeedbackItem item : feedbackItems) {
                if (item.getId().toString().equals(feedbackItemId)) {
                    modelMapper.map(feedbackItemDto, item);
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.error("Feedback item not found: {}", feedbackItemId);
                throw new FeedbackItemNotFoundException("Feedback item not found: " + feedbackItemId);
            }
            retrospective = retrospectiveRepository.save(retrospective);
            log.debug("Feedback item {} updated for retrospective: {}", feedbackItemId, retrospectiveName);
            return modelMapper.map(retrospective, RetrospectiveDto.class);
        } else {
            log.error("Retrospective not found: {}", retrospectiveName);
            throw new RetrospectiveNotFoundException("Retrospective not found: " + retrospectiveName);
        }
    }

    /**
     * Retrieves all retrospectives with pagination.
     *
     * @param page     The page number.
     * @param pageSize The size of each page.
     * @return A page of retrospectives.
     */
    public Page<RetrospectiveDto> getAllRetrospectives(int page, int pageSize) {
        log.info("Fetching all retrospectives with pagination (page={}, pageSize={})", page, pageSize);
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Retrospective> retrospectivesPage = retrospectiveRepository.findAll(pageable);
        List<RetrospectiveDto> retrospectiveDtos = retrospectivesPage.getContent().stream()
                .map(retrospective -> modelMapper.map(retrospective, RetrospectiveDto.class))
                .collect(Collectors.toList());
        log.debug("Retrieved {} retrospectives", retrospectivesPage.getTotalElements());
        return new PageImpl<>(retrospectiveDtos, pageable, retrospectivesPage.getTotalElements());
    }

    /**
     * Searches retrospectives by date with pagination.
     *
     * @param date     The date to search for.
     * @param page     The page number.
     * @param pageSize The size of each page.
     * @return A page of retrospectives.
     */
    public Page<RetrospectiveDto> searchRetrospectivesByDate(LocalDate date, int page, int pageSize) {
        log.info("Searching retrospectives by date (date={}, page={}, pageSize={})", date, page, pageSize);
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Retrospective> retrospectivesPage = retrospectiveRepository.findByDate(date, pageable);
        List<RetrospectiveDto> retrospectiveDtos = retrospectivesPage.getContent().stream()
                .map(retrospective -> modelMapper.map(retrospective, RetrospectiveDto.class))
                .collect(Collectors.toList());
        log.debug("Retrieved {} retrospectives for date {}", retrospectivesPage.getTotalElements(), date);
        return new PageImpl<>(retrospectiveDtos, pageable, retrospectivesPage.getTotalElements());
    }
}
