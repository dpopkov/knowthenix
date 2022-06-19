package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerEntity;
import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.enums.Language;
import io.dpopkov.knowthenix.services.dto.AnswerDto;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AnswerDtoToEntity extends BaseConverter<AnswerDto, AnswerEntity> {

    private final TranslationDtoToAnswerTextEntity translationConverter;

    public AnswerDtoToEntity(TranslationDtoToAnswerTextEntity translationConverter) {
        this.translationConverter = translationConverter;
    }

    @NonNull
    @Override
    public AnswerEntity convert(AnswerDto dto) {
        AnswerEntity entity = new AnswerEntity();
        entity.setId(dto.getId());
        QuestionEntity question = new QuestionEntity();
        question.setId(dto.getQuestionId());
        entity.setQuestion(question);
        SourceEntity source = new SourceEntity();
        source.setId(dto.getSourceId());
        entity.setSource(source);
        entity.setSourceDetails(dto.getSourceDetails());
        entity.setSelectedLanguage(Language.from(dto.getSelectedLanguage()));
        dto.getTranslations().forEach(t -> entity.addTranslation(translationConverter.convert(t)));
        return entity;
    }
}
