package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.enums.SourceType;
import io.dpopkov.knowthenix.services.dto.SourceDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SourceDtoToEntityTest extends ConvertersTestConstants {

    @Test
    void convert() {
        SourceDtoToEntity converter = new SourceDtoToEntity();
        SourceDto dto = new SourceDto();
        dto.setId(ID);
        dto.setName(NAME);
        dto.setFullTitle(FULL_TITLE);
        dto.setUrl(URL);
        dto.setSourceType(SourceType.API_DOC.name());
        dto.setDescription(DESCRIPTION);
        final SourceEntity entity = converter.convert(dto);
        assertNotNull(entity);
        assertEquals(ID, entity.getId());
        assertEquals(NAME, entity.getName());
        assertEquals(FULL_TITLE, entity.getFullTitle());
        assertEquals(URL, entity.getUrl());
        assertEquals(SourceType.API_DOC, entity.getSourceType());
        assertEquals(DESCRIPTION, entity.getDescription());
    }
}
