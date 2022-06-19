package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerTextEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.enums.TextType;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnswerTextEntityToDtoTest extends ConvertersTestConstants {

    @Test
    void convert() {
        AnswerTextEntity entity = new AnswerTextEntity();
        entity.setId(ID);
        entity.setLanguage(Language.EN);
        entity.setType(TextType.MARKDOWN);
        entity.setText(TEXT);

        AnswerTextEntityToDto converter = new AnswerTextEntityToDto();
        TranslationDto dto = converter.convert(entity);
        assertNotNull(dto);
        assertEquals(ID, dto.getId());
        assertEquals(Language.EN.name(), dto.getLanguage());
        assertEquals(TextType.MARKDOWN.name(), dto.getType());
        assertEquals(TEXT, dto.getText());

    }
}
