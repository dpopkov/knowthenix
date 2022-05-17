package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.repositories.CategoryRepository;
import io.dpopkov.knowthenix.services.CategoryService;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto create(CategoryDto dto) {
        ModelMapper mapper = new ModelMapper();
        CategoryEntity entity = mapper.map(dto, CategoryEntity.class);
        CategoryEntity created = categoryRepository.save(entity);
        return mapper.map(created, CategoryDto.class);
    }

    @Override
    public CategoryDto getById(Long id) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new AppServiceException("Category not found"));
        return new ModelMapper().map(entity, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAll() {
        Type typeOfList = new TypeToken<List<CategoryDto>>() {}.getType();
        return new ModelMapper().map(categoryRepository.findAll(), typeOfList);
    }

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
        return new ModelMapper().map(saved, CategoryDto.class);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
