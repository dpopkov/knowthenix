package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.repositories.AppUserRepository;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryDtoToEntityTest extends ConvertersTestConstants {

    @Mock
    AppUserRepository appUserRepository;

    @Test
    void convert() {
        CategoryDtoToEntity converter = new CategoryDtoToEntity(appUserRepository);
        CategoryDto dto = new CategoryDto();
        dto.setId(ID);
        dto.setName(NAME);
        dto.setDescription(DESCRIPTION);
        CategoryEntity entity = converter.convert(dto);
        assertNotNull(entity);
        assertEquals(ID, entity.getId());
        assertEquals(NAME, entity.getName());
        assertEquals(DESCRIPTION, entity.getDescription());
    }
}
