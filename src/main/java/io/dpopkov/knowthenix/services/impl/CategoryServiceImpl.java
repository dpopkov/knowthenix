package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.repositories.CategoryRepository;
import io.dpopkov.knowthenix.services.CategoryService;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import io.dpopkov.knowthenix.services.dto.converters.CategoryDtoToEntity;
import io.dpopkov.knowthenix.services.dto.converters.CategoryEntityToDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDtoToEntity categoryDtoToEntity;
    private final CategoryEntityToDto categoryEntityToDto;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryDtoToEntity categoryDtoToEntity,
                               CategoryEntityToDto categoryEntityToDto) {
        this.categoryRepository = categoryRepository;
        this.categoryDtoToEntity = categoryDtoToEntity;
        this.categoryEntityToDto = categoryEntityToDto;
    }

    @Override
    public CategoryDto create(CategoryDto dto) {
        CategoryEntity entity = categoryDtoToEntity.convert(dto);
        CategoryEntity created = categoryRepository.save(entity);
        return categoryEntityToDto.convert(created);
    }

    @Override
    public CategoryDto getById(Long id) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new AppServiceException("Category not found"));
        return categoryEntityToDto.convert(entity);
    }

    @Override
    public List<CategoryDto> getAll() {
        Iterable<CategoryEntity> all = categoryRepository.findAll();
        List<CategoryDto> list = new ArrayList<>();
        all.forEach(e -> list.add(categoryEntityToDto.convert(e)));
        return list;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public CategoryDto update(CategoryDto dto) {
        Optional<CategoryEntity> byId = categoryRepository.findById(dto.getId());
        if (byId.isEmpty()) {
            throw new AppServiceException("Updated Category not found");
        }
        CategoryEntity entity = byId.get();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        CategoryEntity saved = categoryRepository.save(entity);
        return categoryEntityToDto.convert(saved);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
