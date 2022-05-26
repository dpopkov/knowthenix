package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.enums.TextType;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTextEntityToDtoTest extends ConvertersTestConstants {

    @Test
    void convert() {
        QuestionTextEntity entity = new QuestionTextEntity(Language.EN, TextType.PLAINTEXT, TEXT);
        entity.setId(ID);
        QuestionTextEntityToDto converter = new QuestionTextEntityToDto();
        TranslationDto dto = converter.convert(entity);
        assertNotNull(dto);
        assertEquals(ID, dto.getId());
        assertEquals(Language.EN.name(), dto.getLanguage());
        assertEquals(TextType.PLAINTEXT.name(), dto.getType());
        assertEquals(TEXT, dto.getText());
    }
}
