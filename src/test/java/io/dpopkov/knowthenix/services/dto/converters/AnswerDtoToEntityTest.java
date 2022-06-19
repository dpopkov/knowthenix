package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerEntity;
import io.dpopkov.knowthenix.domain.entities.answer.AnswerTextEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.enums.TextType;
import io.dpopkov.knowthenix.services.dto.AnswerDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnswerDtoToEntityTest extends ConvertersTestConstants {

    @Test
    void convert() {
        AnswerDto dto = new AnswerDto();
        dto.setId(ID);
        dto.setQuestionId(ID_2);
        dto.setSourceId(ID_3);
        dto.setSourceDetails(DETAILS);
        dto.setSelectedLanguage(Language.EN.name());
        TranslationDto trDtoEn = new TranslationDto(Language.EN.name(), TextType.PLAINTEXT.name(), TEXT);
        TranslationDto trDtoRu = new TranslationDto(Language.RU.name(), TextType.PLAINTEXT.name(), TEXT_2);
        dto.addTranslation(trDtoEn);
        dto.addTranslation(trDtoRu);

        AnswerDtoToEntity converter = new AnswerDtoToEntity(new TranslationDtoToAnswerTextEntity());

        AnswerEntity entity = converter.convert(dto);
        assertNotNull(entity);
        assertEquals(ID, entity.getId());
        assertEquals(ID_2, entity.getQuestion().getId());
        assertEquals(ID_3, entity.getSource().getId());
        assertEquals(DETAILS, entity.getSourceDetails());
        assertEquals(Language.EN, entity.getSelectedLanguage());
        Map<Language, AnswerTextEntity> translations = entity.getTranslations();
        assertEquals(2, translations.size());
        assertEquals(TEXT, translations.get(Language.EN).getText());
        assertEquals(TEXT_2, translations.get(Language.RU).getText());
    }
}
