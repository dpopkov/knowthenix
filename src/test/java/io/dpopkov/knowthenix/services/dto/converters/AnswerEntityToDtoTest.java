package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerEntity;
import io.dpopkov.knowthenix.domain.entities.answer.AnswerTextEntity;
import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.services.dto.AnswerDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnswerEntityToDtoTest extends ConvertersTestConstants {

    @Test
    void convert() {
        AnswerEntity entity = new AnswerEntity();
        entity.setId(ID);
        QuestionEntity question = new QuestionEntity();
        question.setId(ID_2);
        entity.setQuestion(question);
        SourceEntity source = new SourceEntity();
        source.setId(ID_3);
        source.setName(NAME);
        entity.setSource(source);
        entity.setSourceDetails(DETAILS);
        entity.setSelectedLanguage(Language.EN);
        AnswerTextEntity textEn = new AnswerTextEntity();
        textEn.setId(ID_4);
        textEn.setLanguage(Language.EN);
        AnswerTextEntity textRu = new AnswerTextEntity();
        textRu.setId(ID_5);
        textRu.setLanguage(Language.RU);
        entity.addTranslation(textEn);
        entity.addTranslation(textRu);

        AnswerEntityToDto converter = new AnswerEntityToDto(new AnswerTextEntityToDto());
        AnswerDto dto = converter.convert(entity);

        assertNotNull(dto);
        assertEquals(ID, dto.getId());
        assertEquals(ID_2, dto.getQuestionId());
        assertEquals(ID_3, dto.getSourceId());
        assertEquals(ID_3, dto.getSourceId());
        assertEquals(NAME, dto.getSourceName());
        assertEquals(DETAILS, dto.getSourceDetails());
        assertEquals(Language.EN.name(), dto.getSelectedLanguage());
        List<TranslationDto> translations = dto.getTranslations();
        assertEquals(2, translations.size());
    }
}
