package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.repositories.CategoryRepository;
import io.dpopkov.knowthenix.services.CategoryServiceException;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
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
class CategoryServiceImplTest {

    private static final Long CATEGORY_ID = 12L;
    private static final String CATEGORY_NAME = "Category";
    private static final String CATEGORY_DESCRIPTION = "Description";

    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryServiceImpl service;
    @Captor
    ArgumentCaptor<CategoryEntity> entityCaptor;

    @Test
    void create() {
        // Given
        given(categoryRepository.save(any(CategoryEntity.class))).willReturn(new CategoryEntity());
        CategoryDto dto = new CategoryDto(CATEGORY_NAME, CATEGORY_DESCRIPTION);
        // When
        CategoryDto created = service.create(dto);
        // Then
        assertNotNull(created);
        then(categoryRepository).should().save(entityCaptor.capture());
        assertEquals(CATEGORY_NAME, entityCaptor.getValue().getName());
    }

    @Test
    void getById() {
        // Given
        CategoryEntity entity = new CategoryEntity();
        entity.setId(CATEGORY_ID);
        given(categoryRepository.findById(CATEGORY_ID)).willReturn(Optional.of(entity));
        // When
        CategoryDto dto = service.getById(CATEGORY_ID);
        // Then
        assertNotNull(dto);
        assertEquals(CATEGORY_ID, dto.getId());
        then(categoryRepository).should().findById(CATEGORY_ID);
    }

    @Test
    void getByIdNotFound() {
        // Given
        given(categoryRepository.findById(CATEGORY_ID)).willReturn(Optional.empty());
        // Then
        assertThrows(CategoryServiceException.class, () -> service.getById(CATEGORY_ID));
    }

    @Test
    void getAll() {
        // Given
        given(categoryRepository.findAll()).willReturn(List.of(new CategoryEntity(), new CategoryEntity()));
        // When
        List<CategoryDto> all = service.getAll();
        // Then
        assertNotNull(all);
        assertEquals(2, all.size());
        then(categoryRepository).should().findAll();
    }

    @Test
    void update() {
        // Given
        given(categoryRepository.save(any())).willReturn(new CategoryEntity());
        // When
        CategoryDto updated = service.update(new CategoryDto());
        // Then
        assertNotNull(updated);
        then(categoryRepository).should().save(any(CategoryEntity.class));
    }

    @Test
    void delete() {
        // When
        service.delete(CATEGORY_ID);
        // Then
        then(categoryRepository).should().deleteById(CATEGORY_ID);
    }
}
