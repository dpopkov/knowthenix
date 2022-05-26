package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static io.dpopkov.knowthenix.config.AppConstants.QUESTIONS_URL;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    @Mock
    QuestionService questionService;
    @InjectMocks
    QuestionController controller;
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllQuestions() throws Exception {
        // When
        mockMvc.perform(get(QUESTIONS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
        // Then
        then(questionService).should().getAll();
    }
}
