package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoToEntityTest extends ConvertersTestConstants {

    @Test
    void convert() {
        CategoryDtoToEntity converter = new CategoryDtoToEntity();
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
