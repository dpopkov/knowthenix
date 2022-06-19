package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerEntity;
import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.repositories.AnswerRepository;
import io.dpopkov.knowthenix.domain.repositories.SourceRepository;
import io.dpopkov.knowthenix.services.dto.AnswerDto;
import io.dpopkov.knowthenix.services.dto.converters.AnswerDtoToEntity;
import io.dpopkov.knowthenix.services.dto.converters.AnswerEntityToDto;
import io.dpopkov.knowthenix.services.dto.converters.AnswerTextEntityToDto;
import io.dpopkov.knowthenix.services.dto.converters.TranslationDtoToAnswerTextEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    private static final Long ANSWER_ID = 11L;
    private static final Long QUESTION_ID = 21L;
    private static final Long SOURCE_ID = 31L;

    @Mock
    AnswerRepository answerRepository;
    @Mock
    SourceRepository sourceRepository;
    AnswerServiceImpl service;
    @Captor
    ArgumentCaptor<AnswerEntity> answerCaptor;

    @BeforeEach
    void setup() {
        service = new AnswerServiceImpl(answerRepository,
                new AnswerDtoToEntity(new TranslationDtoToAnswerTextEntity()),
                new AnswerEntityToDto(new AnswerTextEntityToDto()), sourceRepository);
    }

    @Test
    void create() {
        // Given
        AnswerDto answerDto = new AnswerDto();
        answerDto.setId(ANSWER_ID);
        answerDto.setQuestionId(QUESTION_ID);
        answerDto.setSourceId(SOURCE_ID);
        answerDto.setSelectedLanguage("EN");
        QuestionEntity question = new QuestionEntity();
        question.setId(QUESTION_ID);
        SourceEntity source = new SourceEntity();
        source.setId(SOURCE_ID);
        AnswerEntity entityToReturn = new AnswerEntity();
        entityToReturn.setId(ANSWER_ID);
        entityToReturn.setQuestion(question);
        entityToReturn.setSource(source);
        entityToReturn.setSelectedLanguage(Language.EN);
        given(answerRepository.save(any())).willReturn(entityToReturn);
        given(sourceRepository.findById(SOURCE_ID)).willReturn(Optional.of(new SourceEntity()));
        // When
        final AnswerDto created = service.create(answerDto);
        // Then
        assertNotNull(created);
        assertEquals(ANSWER_ID, created.getId());
        assertEquals(QUESTION_ID, created.getQuestionId());
        assertEquals(SOURCE_ID, created.getSourceId());
        then(answerRepository).should().save(answerCaptor.capture());
        AnswerEntity captured = answerCaptor.getValue();
        assertEquals(ANSWER_ID, captured.getId());
    }

    @Test
    void getById() {
        // Given
        // When
        // Then
    }

    @Test
    void getAll() {
        // Given
        // When
        // Then
    }

    @Test
    void update() {
        // Given
        // When
        // Then
    }

    @Test
    void delete() {
        // Given
        // When
        // Then
    }
}
