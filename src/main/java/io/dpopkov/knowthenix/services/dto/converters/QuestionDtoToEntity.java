package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class QuestionDtoToEntity implements Converter<QuestionDto, QuestionEntity> {

    private final CategoryDtoToEntity categoryDtoToEntity;
    private final TranslationDtoToQuestionTextEntity translationDtoToQuestionTextEntity;

    public QuestionDtoToEntity(CategoryDtoToEntity categoryDtoToEntity, TranslationDtoToQuestionTextEntity translationDtoToQuestionTextEntity) {
        this.categoryDtoToEntity = categoryDtoToEntity;
        this.translationDtoToQuestionTextEntity = translationDtoToQuestionTextEntity;
    }

    @Override
    public QuestionEntity convert(QuestionDto dto) {
        QuestionEntity entity = new QuestionEntity();
        entity.setId(dto.getId());
        entity.setCategory(categoryDtoToEntity.convert(dto.getCategory()));
        entity.setSelectedLanguage(Language.valueOf(dto.getSelectedLanguage()));
        for (TranslationDto tr : dto.getTranslations()) {
            entity.addTranslation(translationDtoToQuestionTextEntity.convert(tr));
        }
        return entity;
    }
}
