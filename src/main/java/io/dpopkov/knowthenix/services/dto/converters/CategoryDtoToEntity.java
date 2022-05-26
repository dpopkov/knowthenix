package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoToEntity extends BaseConverter<CategoryDto, CategoryEntity> {
    @Override
    public CategoryEntity convert(CategoryDto dto) {
        return mapper.map(dto, CategoryEntity.class);
    }
}
