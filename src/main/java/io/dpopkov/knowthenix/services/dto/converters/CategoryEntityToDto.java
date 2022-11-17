package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.entities.user.AppUserEntity;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryEntityToDto extends BaseConverter<CategoryEntity, CategoryDto> {
    @Override
    public CategoryDto convert(CategoryEntity entity) {
        CategoryDto dto = mapper.map(entity, CategoryDto.class);
        AppUserEntity createdBy = entity.getCreatedBy();
        if (createdBy != null) {
            dto.setCreatedBy(createdBy.getUsername());
        } else {
            dto.setCreatedBy(null);
        }
        return dto;
    }
}
