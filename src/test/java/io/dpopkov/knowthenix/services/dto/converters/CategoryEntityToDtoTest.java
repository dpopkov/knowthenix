package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryEntityToDtoTest extends ConvertersTestConstants {

    @Test
    void convert() {
        CategoryEntityToDto converter = new CategoryEntityToDto();
        CategoryEntity entity = new CategoryEntity(NAME, DESCRIPTION);
        entity.setId(ID);
        CategoryDto dto = converter.convert(entity);
        assertNotNull(dto);
        assertEquals(ID, dto.getId());
        assertEquals(NAME, dto.getName());
        assertEquals(DESCRIPTION, dto.getDescription());
    }
}
