package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.springframework.stereotype.Component;

@Component
public class QuestionTextEntityToDto extends BaseConverter<QuestionTextEntity, TranslationDto> {
    @Override
    public TranslationDto convert(QuestionTextEntity entity) {
        return mapper.map(entity, TranslationDto.class);
    }
}
