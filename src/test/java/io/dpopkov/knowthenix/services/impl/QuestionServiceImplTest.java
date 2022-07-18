package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.KeyTermEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.repositories.QuestionRepository;
import io.dpopkov.knowthenix.domain.repositories.QuestionTextRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import io.dpopkov.knowthenix.services.dto.KeyTermDto;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import io.dpopkov.knowthenix.services.dto.converters.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    private static final Long ID_1 = 10L;
    private static final Long ID_2 = 11L;
    private static final Long TRANSLATION_ID_1 = 111L;

    @Mock
    QuestionRepository questionRepository;
    @Mock
    QuestionTextRepository questionTextRepository;
    QuestionServiceImpl service;
    @Captor
    ArgumentCaptor<QuestionEntity> questionEntityCaptor;

    @BeforeEach
    void setupService() {
        final QuestionDtoToEntity questionDtoToEntity = new QuestionDtoToEntity(
                new CategoryDtoToEntity(), new TranslationDtoToQuestionTextEntity());
        service = new QuestionServiceImpl(questionRepository, questionTextRepository,
                new QuestionEntityToDto(new CategoryEntityToDto(), new QuestionTextEntityToDto()), questionDtoToEntity,
                new TranslationDtoToQuestionTextEntity(), new QuestionTextEntityToDto());
    }

    @Test
    void create() {
        // Given
        QuestionDto dto = new QuestionDto();
        dto.setSelectedLanguage("EN");
        dto.setCategory(new CategoryDto("name", "desc"));
        given(questionRepository.save(any(QuestionEntity.class))).willReturn(new QuestionEntity());
        // When
        final QuestionDto created = service.create(dto);
        // Then
        assertNotNull(created);
        then(questionRepository).should().save(any());
    }

    @Test
    void getById() {
        // Given
        var entity = new QuestionEntity();
        entity.setId(ID_1);
        given(questionRepository.findById(ID_1)).willReturn(Optional.of(entity));
        // When
        var found = service.getById(ID_1);
        // Then
        assertNotNull(found);
        assertEquals(ID_1, found.getId());
        then(questionRepository).should().findById(ID_1);
    }

    @Test
    void getAll() {
        // Given
        final QuestionEntity e1 = new QuestionEntity();
        e1.setId(ID_1);
        final QuestionEntity e2 = new QuestionEntity();
        e2.setId(ID_2);
        given(questionRepository.findAll()).willReturn(List.of(e1, e2));
        // When
        List<QuestionDto> all = service.getAll();
        // Then
        assertEquals(2, all.size());
        assertEquals(ID_1, all.get(0).getId());
        assertEquals(ID_2, all.get(1).getId());
        then(questionRepository).should().findAll();
    }

    @Test
    void update() {
        // Given
        given(questionRepository.existsById(ID_1)).willReturn(true);
        given(questionRepository.save(any(QuestionEntity.class))).willReturn(new QuestionEntity());
        QuestionDto dto = new QuestionDto();
        dto.setId(ID_1);
        dto.setCategory(new CategoryDto());
        dto.setSelectedLanguage("EN");
        // When
        final QuestionDto updated = service.update(dto);
        // Then
        assertNotNull(updated);
        then(questionRepository).should().save(questionEntityCaptor.capture());
        QuestionEntity captured = questionEntityCaptor.getValue();
        assertEquals(ID_1, captured.getId());
    }

    @Test
    void update_whenNotFound_thenThrowException() {
        // Given
        QuestionDto dto = new QuestionDto();
        dto.setId(ID_1);
        given(questionRepository.existsById(ID_1)).willReturn(false);
        // When/Then
        assertThrows(AppServiceException.class, () -> service.update(dto));
    }

    @Test
    void addTranslation() {
        // Given
        QuestionEntity foundQuestion = new QuestionEntity();
        foundQuestion.setId(ID_1);
        QuestionTextEntity exitingTranslation = new QuestionTextEntity(Language.RU, "test");
        foundQuestion.addTranslation(exitingTranslation);
        given(questionRepository.findById(ID_1)).willReturn(Optional.of(foundQuestion));
        QuestionTextEntity addedTranslation = new QuestionTextEntity(Language.EN, "test");
        given(questionTextRepository.save(any(QuestionTextEntity.class))).willReturn(addedTranslation);
        given(questionRepository.save(any())).willReturn(new QuestionEntity());
        // When
        service.addTranslation(ID_1, new TranslationDto());
        // Then
        then(questionRepository).should().findById(ID_1);
        then(questionTextRepository).should().save(any(QuestionTextEntity.class));
        then(questionRepository).should().save(questionEntityCaptor.capture());
        QuestionEntity captured = questionEntityCaptor.getValue();
        assertEquals(ID_1, captured.getId());
        assertEquals(2, captured.getTranslations().size());
    }

    @Test
    void addTranslation_whenNoQuestionFound_thenThrowException() {
        // Given
        given(questionRepository.findById(ID_1)).willReturn(Optional.empty());
        // When/Then
        assertThrows(AppServiceException.class, () -> service.addTranslation(ID_1, new TranslationDto()));
    }

    @Test
    void updateTranslation() {
        // Given
        given(questionRepository.existsById(ID_1)).willReturn(true);
        given(questionTextRepository.findById(TRANSLATION_ID_1)).willReturn(Optional.of(new QuestionTextEntity()));
        given(questionTextRepository.save(any())).willReturn(new QuestionTextEntity());
        TranslationDto toUpdate = new TranslationDto("EN", "PLAINTEXT", "text");
        toUpdate.setId(TRANSLATION_ID_1);
        // When
        final TranslationDto updated = service.updateTranslation(ID_1, toUpdate);
        // Then
        assertNotNull(updated);
        then(questionRepository).should().existsById(ID_1);
        then(questionTextRepository).should().findById(TRANSLATION_ID_1);
        then(questionTextRepository).should().save(any(QuestionTextEntity.class));
    }

    @Test
    void updateTranslation_whenNoQuestionFound_thenThrowException() {
        // Given
        given(questionRepository.existsById(ID_1)).willReturn(false);
        // When/Then
        assertThrows(AppServiceException.class, () -> service.updateTranslation(ID_1, new TranslationDto()));
    }

    @Test
    void updateTranslation_whenNoTranslationFound_thenThrowException() {
        // Given
        given(questionRepository.existsById(ID_1)).willReturn(true);
        given(questionTextRepository.findById(TRANSLATION_ID_1)).willReturn(Optional.empty());
        TranslationDto toUpdate = new TranslationDto();
        toUpdate.setId(TRANSLATION_ID_1);
        // When/Then
        assertThrows(AppServiceException.class, () -> service.updateTranslation(ID_1, toUpdate));
    }

    @Test
    void getKeyTermsByQuestionId() {
        // Given
        QuestionEntity question = new QuestionEntity();
        question.addKeyTerm(new KeyTermEntity("1", ""));
        question.addKeyTerm(new KeyTermEntity("2", ""));
        given(questionRepository.findById(ID_1)).willReturn(Optional.of(question));
        // When
        Collection<KeyTermDto> keyTerms = service.getKeyTermsByQuestionId(ID_1);
        // Then
        then(questionRepository).should().findById(ID_1);
        assertEquals(2, keyTerms.size());
    }
}
