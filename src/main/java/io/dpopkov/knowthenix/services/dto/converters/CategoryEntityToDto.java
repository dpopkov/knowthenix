package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryEntityToDto extends BaseConverter<CategoryEntity, CategoryDto> {
    @Override
    public CategoryDto convert(CategoryEntity entity) {
        return mapper.map(entity, CategoryDto.class);
    }
}
