package io.dpopkov.knowthenix.rest.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.dpopkov.knowthenix.services.QuestionService;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static io.dpopkov.knowthenix.config.AppConstants.QUESTIONS_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    private static final Long ID_1 = 12L;

    private final JsonMapper mapper = new JsonMapper();
    @Mock
    QuestionService questionService;
    @InjectMocks
    QuestionController controller;
    @Captor
    ArgumentCaptor<QuestionDto> dtoCaptor;
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

    @Test
    void getById() throws Exception {
        // Given
        given(questionService.getById(ID_1)).willReturn(new QuestionDto());
        // When
        mockMvc.perform(get(QUESTIONS_URL + "/" + ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
        // Then
        then(questionService).should().getById(ID_1);
    }

    @Test
    void update() throws Exception {
        // Given
        QuestionDto putDto = new QuestionDto();
        putDto.setId(ID_1);
        String putJson = mapper.writeValueAsString(putDto);
        QuestionDto returnDto = new QuestionDto();
        returnDto.setId(ID_1);
        given(questionService.update(any(QuestionDto.class))).willReturn(returnDto);
        // When
        mockMvc.perform(put(QUESTIONS_URL)
                .contentType(APPLICATION_JSON)
                .content(putJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
        // Then
        then(questionService).should().update(dtoCaptor.capture());
        QuestionDto captured = dtoCaptor.getValue();
        assertEquals(ID_1, captured.getId());
    }
}
