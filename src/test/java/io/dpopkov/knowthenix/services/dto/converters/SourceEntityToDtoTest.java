package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.enums.SourceType;
import io.dpopkov.knowthenix.services.dto.SourceDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SourceEntityToDtoTest extends ConvertersTestConstants {

    @Test
    void convert() {
        SourceEntityToDto converter = new SourceEntityToDto();
        SourceEntity entity = new SourceEntity();
        entity.setId(ID);
        entity.setName(NAME);
        entity.setFullTitle(FULL_TITLE);
        entity.setUrl(URL);
        entity.setSourceType(SourceType.API_DOC);
        entity.setDescription(DESCRIPTION);
        final SourceDto dto = converter.convert(entity);
        assertNotNull(dto);
        assertEquals(ID, dto.getId());
        assertEquals(NAME, dto.getName());
        assertEquals(FULL_TITLE, dto.getFullTitle());
        assertEquals(URL, dto.getUrl());
        assertEquals(SourceType.API_DOC.name(), dto.getSourceType());
        assertEquals(DESCRIPTION, dto.getDescription());
    }
}
