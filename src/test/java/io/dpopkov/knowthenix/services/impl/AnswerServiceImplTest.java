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

import java.util.List;
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
    void getAllForQuestion() {
        // Given
        QuestionEntity question = new QuestionEntity();
        SourceEntity source = new SourceEntity();
        AnswerEntity e1 = new AnswerEntity();
        e1.setQuestion(question);
        e1.setSource(source);
        AnswerEntity e2 = new AnswerEntity();
        e2.setQuestion(question);
        e2.setSource(source);
        given(answerRepository.findAllByQuestionId(QUESTION_ID)).willReturn(List.of(e1, e2));
        // When
        final List<AnswerDto> allForQuestion = service.getAllForQuestion(QUESTION_ID);
        // Then
        assertNotNull(allForQuestion);
        assertEquals(2, allForQuestion.size());
        then(answerRepository).should().findAllByQuestionId(QUESTION_ID);
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
        AnswerEntity entity = new AnswerEntity();
        entity.setId(ANSWER_ID);
        entity.setQuestion(new QuestionEntity());
        entity.setSource(new SourceEntity());
        given(answerRepository.findById(ANSWER_ID)).willReturn(Optional.of(entity));
        // When
        final AnswerDto answer = service.getById(ANSWER_ID);
        // Then
        assertNotNull(answer);
        assertEquals(ANSWER_ID, answer.getId());
        then(answerRepository).should().findById(ANSWER_ID);
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
        AnswerDto dto = new AnswerDto();
        dto.setId(ANSWER_ID);
        dto.setQuestionId(QUESTION_ID);
        dto.setSourceId(SOURCE_ID);
        dto.setSelectedLanguage(Language.EN.name());
        AnswerEntity entity = new AnswerEntity();
        entity.setId(ANSWER_ID);
        SourceEntity source = new SourceEntity();
        source.setId(SOURCE_ID);
        entity.setSource(source);
        entity.setQuestion(new QuestionEntity());
        given(answerRepository.findById(ANSWER_ID)).willReturn(Optional.of(entity));
        given(answerRepository.save(any(AnswerEntity.class))).willReturn(entity);
        // When
        final AnswerDto updated = service.update(dto);
        // Then
        assertNotNull(updated);
        assertEquals(ANSWER_ID, updated.getId());
        then(answerRepository).should().save(answerCaptor.capture());
        AnswerEntity toSave = answerCaptor.getValue();
        assertEquals(ANSWER_ID, toSave.getId());

    }

    @Test
    void delete() {
        // Given
        // When
        // Then
    }
}
