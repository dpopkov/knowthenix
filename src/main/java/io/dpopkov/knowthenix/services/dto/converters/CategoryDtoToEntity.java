package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.entities.user.AppUserEntity;
import io.dpopkov.knowthenix.domain.repositories.AppUserRepository;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryDtoToEntity extends BaseConverter<CategoryDto, CategoryEntity> {

    private final AppUserRepository appUserRepository;

    public CategoryDtoToEntity(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public CategoryEntity convert(CategoryDto dto) {
        CategoryEntity entity = mapper.map(dto, CategoryEntity.class);
        Optional<AppUserEntity> byUsername = appUserRepository.findByUsername(dto.getCreatedBy());
        if (byUsername.isPresent()) {
            entity.setCreatedBy(byUsername.get());
        } else {
            entity.setCreatedBy(null);
        }
        return entity;
    }
}
