package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.springframework.stereotype.Component;

@Component
public class TranslationDtoToQuestionTextEntity extends BaseConverter<TranslationDto, QuestionTextEntity> {
    @Override
    public QuestionTextEntity convert(TranslationDto dto) {
        return mapper.map(dto, QuestionTextEntity.class);
    }
}
