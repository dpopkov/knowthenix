package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.enums.TextType;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TranslationDtoToQuestionTextEntityTest extends ConvertersTestConstants {

    @Test
    void convert() {
        TranslationDto dto = new TranslationDto(Language.EN.name(), TextType.PLAINTEXT.name(), TEXT);
        dto.setId(ID);
        TranslationDtoToQuestionTextEntity converter = new TranslationDtoToQuestionTextEntity();
        QuestionTextEntity entity = converter.convert(dto);
        assertNotNull(entity);
        assertEquals(ID, entity.getId());
        assertEquals(Language.EN, entity.getLanguage());
        assertEquals(TextType.PLAINTEXT, entity.getType());
        assertEquals(TEXT, entity.getText());
    }
}
