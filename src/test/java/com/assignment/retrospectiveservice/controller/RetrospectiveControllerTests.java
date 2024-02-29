package com.assignment.retrospectiveservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.assignment.retrospectiveservice.dto.FeedbackItemDto;
import com.assignment.retrospectiveservice.dto.RetrospectiveDto;
import com.assignment.retrospectiveservice.exception.FeedbackItemNotFoundException;
import com.assignment.retrospectiveservice.exception.RetrospectiveNotFoundException;
import com.assignment.retrospectiveservice.model.FeedbackType;
import com.assignment.retrospectiveservice.service.RetrospectiveService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RetrospectiveController.class)
class RetrospectiveControllerTests {

        @MockBean
        private RetrospectiveService retrospectiveService;

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void testCreateRetrospective() throws Exception {
                // Prepare test data
                RetrospectiveDto retrospectiveDto = new RetrospectiveDto();
                retrospectiveDto.setName("Retrospective 1");
                retrospectiveDto.setSummary("Summary of retrospective 1");
                retrospectiveDto.setDate(LocalDate.now());
                retrospectiveDto.setParticipants(Arrays.asList("Chandra", "Balu"));
                retrospectiveDto.setFeedbackItems(null);

                // Mock service response
                when(retrospectiveService.createRetrospective(any(RetrospectiveDto.class)))
                                .thenReturn(retrospectiveDto);

                // Perform POST request
                mockMvc.perform(MockMvcRequestBuilders.post("/retrospectives")
                                .content(objectMapper.writeValueAsString(retrospectiveDto))
                                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.name").value(retrospectiveDto.getName()))
                                .andExpect(jsonPath("$.summary").value(retrospectiveDto.getSummary()))
                                .andExpect(jsonPath("$.date").value(retrospectiveDto.getDate().toString()))
                                .andExpect(jsonPath("$.participants").isArray())
                                .andExpect(jsonPath("$.participants[0]")
                                                .value(retrospectiveDto.getParticipants().get(0)))
                                .andExpect(jsonPath("$.participants[1]")
                                                .value(retrospectiveDto.getParticipants().get(1)))
                                .andExpect(jsonPath("$.feedbackItems").doesNotExist());
        }

        @Test
        void testAddFeedbackItem_Success() throws Exception {
                // Prepare test data
                String retrospectiveName = "Retrospective 1";

                // Mocking values for RetrospectiveDto
                RetrospectiveDto retrospectiveDto = new RetrospectiveDto();
                retrospectiveDto.setName("Sprint 13 Retrospective");
                retrospectiveDto.setSummary("Evaluation of the second sprint");
                retrospectiveDto.setDate(LocalDate.of(2024, 4, 10));
                retrospectiveDto.setParticipants(Arrays.asList("Chandra Penta", "balu", "pramod"));
                retrospectiveDto.setFeedbackItems(Collections.singletonList(
                                new FeedbackItemDto("chandu", "This is the feedback body", FeedbackType.POSITIVE)));

                FeedbackItemDto feedbackItemDto = new FeedbackItemDto("Chandra Penta", "went live!",
                                FeedbackType.POSITIVE);

                // Mock service response
                when(retrospectiveService.addFeedbackItem(eq(retrospectiveName), any(FeedbackItemDto.class)))
                                .thenReturn(retrospectiveDto);

                // Perform POST request
                mockMvc.perform(MockMvcRequestBuilders
                                .post("/retrospectives/{retrospectiveName}/feedback", retrospectiveName)
                                .content(objectMapper.writeValueAsString(feedbackItemDto))
                                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sprint 13 Retrospective"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.summary")
                                                .value("Evaluation of the second sprint"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.date").exists())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2024-04-10"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.participants").isArray())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.participants[0]").value("Chandra Penta"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.participants[1]").value("balu"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.participants[2]").value("pramod"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.feedbackItems").isArray())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.feedbackItems[0].name").value("chandu"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.feedbackItems[0].body")
                                                .value("This is the feedback body"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.feedbackItems[0].feedbackType")
                                                .value("POSITIVE"));
        }

        @Test
        void testAddFeedbackItem_RetrospectiveNotFound() throws Exception {
                // Prepare test data
                String retrospectiveName = "NonExistentRetrospective";
                FeedbackItemDto feedbackItemDto = new FeedbackItemDto("Chandra Penta", "Great job!",
                                FeedbackType.POSITIVE);

                // Mock service response
                when(retrospectiveService.addFeedbackItem(eq(retrospectiveName), any(FeedbackItemDto.class)))
                                .thenThrow(new RetrospectiveNotFoundException(
                                                "Retrospective not found: " + retrospectiveName));

                // Perform POST request and expect 404
                mockMvc.perform(MockMvcRequestBuilders
                                .post("/retrospectives/{retrospectiveName}/feedback", retrospectiveName)
                                .content(objectMapper.writeValueAsString(feedbackItemDto))
                                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void testUpdateFeedbackItem_Success() throws Exception {
                // Prepare test data
                String retrospectiveName = "Retrospective 1";
                String feedbackItemId = "1";
                FeedbackItemDto feedbackItemDto = new FeedbackItemDto("Chandra Penta", "Great job!",
                                FeedbackType.POSITIVE);

                // Mock service response
                RetrospectiveDto updatedRetrospectiveDto = new RetrospectiveDto();
                updatedRetrospectiveDto.setName("Sprint 13 Retrospective");
                updatedRetrospectiveDto.setSummary("summary");
                updatedRetrospectiveDto.setDate(LocalDate.of(2024, 4, 15));
                updatedRetrospectiveDto.setParticipants(Arrays.asList("pramod", "balu"));
                FeedbackItemDto updatedFeedbackItemDto = new FeedbackItemDto("Chandra Penta", "Great job!",
                                FeedbackType.POSITIVE);
                updatedRetrospectiveDto.setFeedbackItems(Collections.singletonList(updatedFeedbackItemDto));

                when(retrospectiveService.updateFeedbackItem(eq(retrospectiveName), eq(feedbackItemId),
                                any(FeedbackItemDto.class))).thenReturn(updatedRetrospectiveDto);

                // Perform PUT request
                mockMvc.perform(MockMvcRequestBuilders
                                .put("/retrospectives/{retrospectiveName}/feedback/{feedbackItemId}",
                                                retrospectiveName, feedbackItemId)
                                .content(objectMapper.writeValueAsString(feedbackItemDto))
                                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sprint 13 Retrospective"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.summary").value("summary"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2024-04-15"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.participants").isArray())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.participants[0]").value("pramod"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.participants[1]").value("balu"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.feedbackItems").isArray())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.feedbackItems[0].name")
                                                .value("Chandra Penta"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.feedbackItems[0].body")
                                                .value("Great job!"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.feedbackItems[0].feedbackType")
                                                .value("POSITIVE"));
        }

        @Test
        void testUpdateFeedbackItem_RetrospectiveNotFound() throws Exception {
                // Prepare test data
                String retrospectiveName = "NonExistentRetrospective";
                String feedbackItemId = "1";
                FeedbackItemDto feedbackItemDto = new FeedbackItemDto("Chandra Penta", "Great job!",
                                FeedbackType.POSITIVE);

                // Mock service response
                when(retrospectiveService.updateFeedbackItem(eq(retrospectiveName), eq(feedbackItemId),
                                any(FeedbackItemDto.class)))
                                .thenThrow(new RetrospectiveNotFoundException(
                                                "Retrospective not found: " + retrospectiveName));

                // Perform PUT request and expect 404
                mockMvc.perform(MockMvcRequestBuilders
                                .put("/retrospectives/{retrospectiveName}/feedback/{feedbackItemId}",
                                                retrospectiveName, feedbackItemId)
                                .content(objectMapper.writeValueAsString(feedbackItemDto))
                                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void testUpdateFeedbackItem_FeedbackItemNotFound() throws Exception {
                // Prepare test data
                String retrospectiveName = "Retrospective 1";
                String feedbackItemId = "NonExistentFeedbackItem";
                FeedbackItemDto feedbackItemDto = new FeedbackItemDto("Chandra Penta", "Great job!",
                                FeedbackType.POSITIVE);

                // Mock service response
                when(retrospectiveService.updateFeedbackItem(eq(retrospectiveName), eq(feedbackItemId),
                                any(FeedbackItemDto.class)))
                                .thenThrow(new FeedbackItemNotFoundException(
                                                "Feedback item not found: " + feedbackItemId));

                // Perform PUT request and expect 404
                mockMvc.perform(MockMvcRequestBuilders
                                .put("/retrospectives/{retrospectiveName}/feedback/{feedbackItemId}",
                                                retrospectiveName, feedbackItemId)
                                .content(objectMapper.writeValueAsString(feedbackItemDto))
                                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void testGetAllRetrospectives() throws Exception {
                // Prepare test data
                RetrospectiveDto retrospectiveDto = new RetrospectiveDto();
                retrospectiveDto.setName("Retrospective 1");

                Page<RetrospectiveDto> page = new PageImpl<>(Collections.singletonList(retrospectiveDto));

                // Mock service response
                when(retrospectiveService.getAllRetrospectives(anyInt(), anyInt())).thenReturn(page);

                // Perform GET request
                mockMvc.perform(MockMvcRequestBuilders.get("/retrospectives")
                                .param("page", "0")
                                .param("pageSize", "10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name")
                                                .value(retrospectiveDto.getName()));
        }

        @Test
        void testSearchRetrospectivesByDate() throws Exception {
                // Prepare test data
                RetrospectiveDto retrospectiveDto = new RetrospectiveDto();
                retrospectiveDto.setName("Retrospective 1");

                Page<RetrospectiveDto> page = new PageImpl<>(Collections.singletonList(retrospectiveDto));

                // Mock service response
                when(retrospectiveService.searchRetrospectivesByDate(any(LocalDate.class), anyInt(), anyInt()))
                                .thenReturn(page);

                // Perform GET request
                mockMvc.perform(MockMvcRequestBuilders.get("/retrospectives/search")
                                .param("date", "2023-01-01")
                                .param("page", "0")
                                .param("pageSize", "10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name")
                                                .value(retrospectiveDto.getName()));
        }
}
