package io.dpopkov.knowthenix.rest.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.dpopkov.knowthenix.rest.exceptions.AppControllerException;
import io.dpopkov.knowthenix.services.QuestionService;
import io.dpopkov.knowthenix.services.dto.*;
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

import java.util.List;

import static io.dpopkov.knowthenix.config.AppConstants.QUESTIONS_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    private static final Long ID_1 = 12L;
    private static final Long TRANSLATION_ID = 123L;
    private static final Long KEY_TERM_ID_1 = 1234L;
    private static final Long KEY_TERM_ID_2 = 1235L;

    private final JsonMapper mapper = new JsonMapper();
    @Mock
    QuestionService questionService;
    @InjectMocks
    QuestionController controller;
    @Captor
    ArgumentCaptor<QuestionDto> dtoCaptor;
    @Captor
    ArgumentCaptor<Long> questionIdCaptor;
    @Captor
    ArgumentCaptor<IdChangeSetDto> changeSetCaptor;
    @Captor
    ArgumentCaptor<Long> idCaptor;
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
    void addNewQuestion() throws Exception {
        // Given
        QuestionDto postDto = new QuestionDto();
        postDto.setCategory(new CategoryDto("c1", "d1"));
        String postJson = mapper.writeValueAsString(postDto);
        QuestionDto returnDto = new QuestionDto();
        returnDto.setId(ID_1);
        given(questionService.create(any(QuestionDto.class))).willReturn(returnDto);
        // When
        mockMvc.perform(post(QUESTIONS_URL)
                .contentType(APPLICATION_JSON)
                .content(postJson)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));
        // Then
        then(questionService).should().create(any(QuestionDto.class));
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

    @Test
    void addTranslation() throws Exception {
        // Given
        TranslationDto postDto = new TranslationDto();
        String postJson = mapper.writeValueAsString(postDto);
        TranslationDto returnDto = new TranslationDto();
        returnDto.setId(TRANSLATION_ID);
        given(questionService.addTranslation(anyLong(), any(TranslationDto.class))).willReturn(returnDto);
        // When
        mockMvc.perform(post(QUESTIONS_URL + "/" + ID_1 + "/translations")
                .contentType(APPLICATION_JSON)
                .content(postJson)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));
        // Then
        then(questionService).should().addTranslation(anyLong(), any(TranslationDto.class));
    }

    @Test
    void updateTranslation() throws Exception {
        // Given
        TranslationDto putDto = new TranslationDto();
        putDto.setId(TRANSLATION_ID);
        String putJson = mapper.writeValueAsString(putDto);
        TranslationDto returnDto = new TranslationDto();
        given(questionService.updateTranslation(anyLong(), any(TranslationDto.class))).willReturn(returnDto);
        // When
        mockMvc.perform(put(QUESTIONS_URL + "/" + ID_1 + "/translations")
                .contentType(APPLICATION_JSON)
                .content(putJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
        // Then
        then(questionService).should().updateTranslation(questionIdCaptor.capture(), any(TranslationDto.class));
        assertEquals(ID_1, questionIdCaptor.getValue());
    }

    @Test
    void updateTranslation_whenNoTranslationId_thenThrowException() throws Exception {
        // Given
        TranslationDto putDto = new TranslationDto();
        String putJson = mapper.writeValueAsString(putDto);
        // When/Then
        mockMvc.perform(put(QUESTIONS_URL + "/" + ID_1 + "/translations")
                .contentType(APPLICATION_JSON)
                .content(putJson)
        )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AppControllerException));
    }

    @Test
    void getKeyTerms() throws Exception {
        // Given
        given(questionService.getKeyTermsByQuestionId(ID_1)).willReturn(List.of(new KeyTermDto("1", "")));
        // When
        mockMvc.perform(get(QUESTIONS_URL + "/" + ID_1 + "/keyterms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
        // Then
        then(questionService).should().getKeyTermsByQuestionId(ID_1);
    }

    @Test
    void changeKeyTerms() throws Exception {
        // Given
        IdChangeSetDto dto = new IdChangeSetDto();
        dto.getAdd().add(KEY_TERM_ID_1);
        dto.getRemove().add(KEY_TERM_ID_2);
        String patchJson = mapper.writeValueAsString(dto);
        // When
        mockMvc.perform(patch(QUESTIONS_URL + "/" + ID_1 + "/keyterms")
                .contentType(APPLICATION_JSON)
                .content(patchJson)
        )
                .andExpect(status().isOk());
        // Then
        then(questionService).should().changeKeyTermsByQuestionId(idCaptor.capture(), changeSetCaptor.capture());
        assertEquals(ID_1, idCaptor.getValue());
        IdChangeSetDto captured = changeSetCaptor.getValue();
        assertEquals(KEY_TERM_ID_1, captured.getAdd().get(0));
        assertEquals(KEY_TERM_ID_2, captured.getRemove().get(0));
    }
}
