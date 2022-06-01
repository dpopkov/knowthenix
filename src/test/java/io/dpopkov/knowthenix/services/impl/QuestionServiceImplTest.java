package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.repositories.QuestionRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.converters.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    private static final Long ID_1 = 10L;
    private static final Long ID_2 = 11L;

    @Mock
    QuestionRepository questionRepository;
    QuestionServiceImpl service;
    @Captor
    ArgumentCaptor<QuestionEntity> entityCaptor;

    @BeforeEach
    void setupService() {
        final QuestionDtoToEntity questionDtoToEntity = new QuestionDtoToEntity(
                new CategoryDtoToEntity(), new TranslationDtoToQuestionTextEntity());
        service = new QuestionServiceImpl(questionRepository,
                new QuestionEntityToDto(new CategoryEntityToDto(), new QuestionTextEntityToDto()), questionDtoToEntity);
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
        then(questionRepository).should().save(entityCaptor.capture());
        QuestionEntity captured = entityCaptor.getValue();
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
}
