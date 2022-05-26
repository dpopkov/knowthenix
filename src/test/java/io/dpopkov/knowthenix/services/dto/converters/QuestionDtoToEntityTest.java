package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.enums.TextType;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QuestionDtoToEntityTest extends ConvertersTestConstants {

    private final QuestionDtoToEntity converter = new QuestionDtoToEntity(
            new CategoryDtoToEntity(),
            new TranslationDtoToQuestionTextEntity());

    @Test
    void convert() {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(ID);
        CategoryDto categoryDto = new CategoryDto(NAME, DESCRIPTION);
        categoryDto.setId(ID_2);
        questionDto.setCategory(categoryDto);
        questionDto.setSelectedLanguage(Language.RU.name());
        final TranslationDto translationDtoEn = new TranslationDto(Language.EN.name(), TextType.PLAINTEXT.name(), TEXT);
        translationDtoEn.setId(ID_3);
        questionDto.addTranslation(translationDtoEn);
        final TranslationDto translationDtoRu = new TranslationDto(Language.RU.name(), TextType.HTML.name(), TEXT_2);
        translationDtoRu.setId(ID_4);
        questionDto.addTranslation(translationDtoRu);

        final QuestionEntity questionEntity = converter.convert(questionDto);

        assertNotNull(questionEntity);
        assertEquals(ID, questionEntity.getId());
        assertEquals(ID_2, questionEntity.getCategory().getId());
        assertEquals(NAME, questionEntity.getCategory().getName());
        assertEquals(DESCRIPTION, questionEntity.getCategory().getDescription());
        assertEquals(Language.RU, questionEntity.getSelectedLanguage());
        final Map<Language, QuestionTextEntity> translations = questionEntity.getTranslations();
        assertEquals(2, translations.size());

        final QuestionTextEntity questionTextEn = translations.get(Language.EN);
        assertEquals(ID_3, questionTextEn.getId());
        assertEquals(Language.EN, questionTextEn.getLanguage());
        assertEquals(TextType.PLAINTEXT, questionTextEn.getType());
        assertEquals(TEXT, questionTextEn.getText());

        final QuestionTextEntity questionTextRu = translations.get(Language.RU);
        assertEquals(ID_4, questionTextRu.getId());
        assertEquals(Language.RU, questionTextRu.getLanguage());
        assertEquals(TextType.HTML, questionTextRu.getType());
        assertEquals(TEXT_2, questionTextRu.getText());
    }
}
