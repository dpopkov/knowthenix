package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.enums.SourceType;
import io.dpopkov.knowthenix.domain.repositories.SourceRepository;
import io.dpopkov.knowthenix.services.dto.SourceDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SourceServiceImplTest {

    private static final Long SOURCE_ID = 12L;
    private static final String SOURCE_NAME = "Source name";
    private static final String SOURCE_FULL_TITLE = "Source full title";
    private static final String SOURCE_URL = "http://www.example.org";
    private static final String SOURCE_TYPE = SourceType.API_DOC.name();
    private static final String SOURCE_DESCRIPTION = "Source description";

    @Mock
    SourceRepository sourceRepository;
    @InjectMocks
    SourceServiceImpl service;
    @Captor
    ArgumentCaptor<SourceEntity> sourceEntityCaptor;

    @Test
    void create() {
        // Given
        given(sourceRepository.save(any(SourceEntity.class))).willReturn(new SourceEntity());
        SourceDto dto = new SourceDto(SOURCE_NAME, SOURCE_FULL_TITLE, SOURCE_URL, SOURCE_TYPE, SOURCE_DESCRIPTION);
        // When
        SourceDto created = service.create(dto);
        // Then
        assertNotNull(created);
        then(sourceRepository).should().save(sourceEntityCaptor.capture());
        final SourceEntity saved = sourceEntityCaptor.getValue();
        assertEquals(SOURCE_NAME, saved.getName());
        assertEquals(SourceType.API_DOC, saved.getSourceType());
    }

    @Test
    void getById() {
        // Given
        SourceEntity entity = new SourceEntity();
        entity.setId(SOURCE_ID);
        given(sourceRepository.findById(SOURCE_ID)).willReturn(Optional.of(entity));
        // When
        final SourceDto found = service.getById(SOURCE_ID);
        // Then
        assertNotNull(found);
        assertEquals(SOURCE_ID, found.getId());
        then(sourceRepository).should().findById(SOURCE_ID);
    }

    @Test
    void getAll() {
        // Given
        given(sourceRepository.findAll()).willReturn(List.of(new SourceEntity(), new SourceEntity()));
        // When
        final List<SourceDto> all = service.getAll();
        // Then
        assertNotNull(all);
        assertEquals(2, all.size());
        then(sourceRepository).should().findAll();
    }

    @Test
    void update() {
        // Given
        SourceEntity entity = new SourceEntity();
        entity.setId(SOURCE_ID);
        given(sourceRepository.findById(SOURCE_ID)).willReturn(Optional.of(entity));
        SourceDto toUpdate = new SourceDto();
        toUpdate.setId(SOURCE_ID);
        given(sourceRepository.save(any())).willReturn(entity);
        // When
        final SourceDto updated = service.update(toUpdate);
        // Then
        assertNotNull(updated);
        assertEquals(SOURCE_ID, updated.getId());
        then(sourceRepository).should().save(sourceEntityCaptor.capture());
        SourceEntity captured = sourceEntityCaptor.getValue();
        assertEquals(SOURCE_ID, captured.getId());
    }

    @Test
    void delete() {
        // When
        service.delete(SOURCE_ID);
        // Then
        then(sourceRepository).should().deleteById(SOURCE_ID);
    }
}
