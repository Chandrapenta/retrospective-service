package com.assignment.retrospectiveservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import com.assignment.retrospectiveservice.dto.*;
import com.assignment.retrospectiveservice.exception.*;
import com.assignment.retrospectiveservice.model.*;
import com.assignment.retrospectiveservice.repository.*;

@ExtendWith(MockitoExtension.class)
class RetrospectiveServiceTests {

    private RetrospectiveRepository retrospectiveRepository;
    private ModelMapper modelMapper;
    private RetrospectiveService retrospectiveService;

    @BeforeEach
    void setUp() {
        retrospectiveRepository = mock(RetrospectiveRepository.class);
        modelMapper = new ModelMapper();
        retrospectiveService = new RetrospectiveService(retrospectiveRepository, modelMapper);
    }

    @Test
    void testCreateRetrospective_Success() {
        // Prepare test data
        RetrospectiveDto retrospectiveDto = new RetrospectiveDto();
        retrospectiveDto.setName("Retrospective 1");
        retrospectiveDto.setDate(LocalDate.now());
        retrospectiveDto.setParticipants(Collections.singletonList("Participant 1"));

        when(retrospectiveRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(retrospectiveRepository.save(any())).thenReturn(modelMapper.map(retrospectiveDto, Retrospective.class));

        // Call service method
        RetrospectiveDto createdRetrospectiveDto = retrospectiveService.createRetrospective(retrospectiveDto);

        // Assertions
        assertEquals(retrospectiveDto.getName(), createdRetrospectiveDto.getName());
    }

    @Test
    void testCreateRetrospective_RetrospectiveAlreadyExists() {
        // Prepare test data
        RetrospectiveDto retrospectiveDto = new RetrospectiveDto();
        retrospectiveDto.setName("Retrospective 1");
        retrospectiveDto.setDate(LocalDate.now());
        retrospectiveDto.setParticipants(Collections.singletonList("Participant 1"));

        when(retrospectiveRepository.findByName(anyString()))
                .thenReturn(Optional.of(new Retrospective()));

        // Assertions
        assertThrows(RetrospectiveAlreadyExistsException.class,
                () -> retrospectiveService.createRetrospective(retrospectiveDto));
    }

    @Test
    void testAddFeedbackItem_Success() {
        // Prepare test data
        String retrospectiveName = "Retrospective 1";
        FeedbackItemDto feedbackItemDto = new FeedbackItemDto();
        Retrospective retrospective = new Retrospective();

        when(retrospectiveRepository.findByName(anyString())).thenReturn(Optional.of(retrospective));
        when(retrospectiveRepository.save(any())).thenReturn(retrospective);

        // Call service method
        RetrospectiveDto updatedRetrospectiveDto = retrospectiveService.addFeedbackItem(retrospectiveName,
                feedbackItemDto);

        // Assertions
        assertNotNull(updatedRetrospectiveDto);
    }

    @Test
    void testAddFeedbackItem_RetrospectiveNotFound() {
        // Prepare test data
        String retrospectiveName = "NonExistentRetrospective";
        FeedbackItemDto feedbackItemDto = new FeedbackItemDto();

        when(retrospectiveRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Assertions
        assertThrows(RetrospectiveNotFoundException.class,
                () -> retrospectiveService.addFeedbackItem(retrospectiveName, feedbackItemDto));
    }

    @Test
    void testUpdateFeedbackItem_Success() {
        // Prepare test data
        String retrospectiveName = "Retrospective 1";
        FeedbackItemDto feedbackItemDto = new FeedbackItemDto();

        Retrospective retrospective = new Retrospective();
        retrospective.setId(UUID.randomUUID());
        retrospective.setFeedbackItems(new ArrayList<>());
        FeedbackItem feedbackItem = new FeedbackItem();
        UUID feedbackItemIdUUID = UUID.randomUUID();
        feedbackItem.setId(feedbackItemIdUUID);
        retrospective.getFeedbackItems().add(feedbackItem);

        when(retrospectiveRepository.findByName(anyString())).thenReturn(Optional.of(retrospective));
        when(retrospectiveRepository.save(any())).thenReturn(retrospective);

        // Call service method
        RetrospectiveDto updatedRetrospectiveDto = retrospectiveService.updateFeedbackItem(retrospectiveName,
                feedbackItemIdUUID.toString(), feedbackItemDto);

        // Assertions
        assertNotNull(updatedRetrospectiveDto);
    }

    @Test
    void testUpdateFeedbackItem_RetrospectiveNotFound() {
        // Prepare test data
        String retrospectiveName = "NonExistentRetrospective";
        String feedbackItemId = "1";
        FeedbackItemDto feedbackItemDto = new FeedbackItemDto();

        when(retrospectiveRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Assertions
        assertThrows(RetrospectiveNotFoundException.class,
                () -> retrospectiveService.updateFeedbackItem(retrospectiveName, feedbackItemId, feedbackItemDto));
    }

    @Test
    void testUpdateFeedbackItem_FeedbackItemNotFound() {
        // Prepare test data
        String retrospectiveName = "Retrospective 1";
        String feedbackItemId = "NonExistentFeedbackItem";
        FeedbackItemDto feedbackItemDto = new FeedbackItemDto();

        Retrospective retrospective = new Retrospective();
        retrospective.setId(UUID.randomUUID());
        retrospective.setFeedbackItems(new ArrayList<>());

        when(retrospectiveRepository.findByName(anyString())).thenReturn(Optional.of(retrospective));

        // Assertions
        assertThrows(FeedbackItemNotFoundException.class,
                () -> retrospectiveService.updateFeedbackItem(retrospectiveName, feedbackItemId, feedbackItemDto));
    }

    @Test
    void testGetAllRetrospectives() {
        // Prepare test data
        Pageable pageable = PageRequest.of(0, 10);
        List<Retrospective> retrospectives = Collections.singletonList(new Retrospective());
        Page<Retrospective> page = new PageImpl<>(retrospectives);

        when(retrospectiveRepository.findAll(pageable)).thenReturn(page);

        // Call service method
        Page<RetrospectiveDto> resultPage = retrospectiveService.getAllRetrospectives(0, 10);

        // Assertions
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getContent().size());
    }

    @Test
    void testSearchRetrospectivesByDate() {
        // Prepare test data
        LocalDate date = LocalDate.now();
        // Pageable pageable = PageRequest.of(0, 10);
        List<Retrospective> retrospectives = Collections.singletonList(new Retrospective());
        Page<Retrospective> page = new PageImpl<>(retrospectives);

        when(retrospectiveRepository.findByDate(eq(date), any(Pageable.class))).thenReturn(page);

        // Call service method
        Page<RetrospectiveDto> resultPage = retrospectiveService.searchRetrospectivesByDate(date, 0, 10);

        // Assertions
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getContent().size());
    }
}
