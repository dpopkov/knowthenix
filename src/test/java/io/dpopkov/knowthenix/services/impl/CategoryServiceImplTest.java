package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.repositories.CategoryRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import io.dpopkov.knowthenix.services.dto.converters.CategoryDtoToEntity;
import io.dpopkov.knowthenix.services.dto.converters.CategoryEntityToDto;
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
    @Mock
    CategoryDtoToEntity categoryDtoToEntity;
    @Mock
    CategoryEntityToDto categoryEntityToDto;
    @InjectMocks
    CategoryServiceImpl service;
    @Captor
    ArgumentCaptor<CategoryEntity> entityCaptor;

    @Test
    void create() {
        // Given
        given(categoryDtoToEntity.convert(any())).willReturn(new CategoryEntity(CATEGORY_NAME));
        given(categoryRepository.save(any())).willReturn(new CategoryEntity(CATEGORY_NAME));
        CategoryDto dto = new CategoryDto(CATEGORY_NAME, CATEGORY_DESCRIPTION);
        given(categoryEntityToDto.convert(any())).willReturn(dto);
        // When
        CategoryDto created = service.create(dto);
        // Then
        assertNotNull(created);
        then(categoryRepository).should().save(entityCaptor.capture());
        assertEquals(CATEGORY_NAME, entityCaptor.getValue().getName());
    }

    @Test
    void getByIdNotFound() {
        // Given
        given(categoryRepository.findById(CATEGORY_ID)).willReturn(Optional.empty());
        // Then
        assertThrows(AppServiceException.class, () -> service.getById(CATEGORY_ID));
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
    void update_whenNotFound_throwsException() {
        // Given
        CategoryDto dto = new CategoryDto();
        dto.setId(CATEGORY_ID);
        given(categoryRepository.findById(CATEGORY_ID)).willReturn(Optional.empty());
        // Then
        assertThrows(AppServiceException.class, () -> service.update(dto));
    }

    @Test
    void delete() {
        // When
        service.delete(CATEGORY_ID);
        // Then
        then(categoryRepository).should().deleteById(CATEGORY_ID);
    }
}
