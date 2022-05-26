package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.domain.enums.TextType;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionEntityToDtoTest extends ConvertersTestConstants {

    private final QuestionEntityToDto converter = new QuestionEntityToDto(
            new CategoryEntityToDto(),
            new QuestionTextEntityToDto()
    );

    @Test
    void convert() {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setId(ID);
        CategoryEntity categoryEntity = new CategoryEntity(NAME, DESCRIPTION);
        categoryEntity.setId(ID_2);
        questionEntity.setCategory(categoryEntity);
        questionEntity.setSelectedLanguage(Language.EN);
        QuestionTextEntity text1 = new QuestionTextEntity(Language.EN, TextType.PLAINTEXT, TEXT);
        QuestionTextEntity text2 = new QuestionTextEntity(Language.RU, TextType.MARKDOWN, TEXT_2);
        questionEntity.addTranslation(text1);
        questionEntity.addTranslation(text2);

        final QuestionDto questionDto = converter.convert(questionEntity);

        assertNotNull(questionDto);
        assertEquals(ID, questionDto.getId());
        assertEquals(ID_2, questionDto.getCategory().getId());
        assertEquals(NAME, questionDto.getCategory().getName());
        assertEquals(DESCRIPTION, questionDto.getCategory().getDescription());
        assertEquals(Language.EN.name(), questionDto.getSelectedLanguage());
        final List<TranslationDto> translations = questionDto.getTranslations();
        assertNotNull(translations);
        assertEquals(2, translations.size());
        final TranslationDto translationDto1 = translations.get(0);
        final TranslationDto translationDto2 = translations.get(1);

        TranslationDto translationEn = translationDto1.getLanguage().equals(Language.EN.name()) ?
                translationDto1 : translationDto2;
        assertEquals(Language.EN.name(), translationEn.getLanguage());
        assertEquals(TextType.PLAINTEXT.name(), translationEn.getType());
        assertEquals(TEXT, translationEn.getText());

        TranslationDto translationRu = translationDto1.getLanguage().equals(Language.RU.name()) ?
                translationDto1 : translationDto2;
        assertEquals(Language.RU.name(), translationRu.getLanguage());
        assertEquals(TextType.MARKDOWN.name(), translationRu.getType());
        assertEquals(TEXT_2, translationRu.getText());
    }
}
