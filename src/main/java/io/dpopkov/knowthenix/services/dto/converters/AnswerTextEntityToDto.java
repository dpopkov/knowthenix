package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerTextEntity;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.springframework.stereotype.Component;

@Component
public class AnswerTextEntityToDto extends BaseConverter<AnswerTextEntity, TranslationDto> {
    @Override
    public TranslationDto convert(AnswerTextEntity entity) {
        return mapper.map(entity, TranslationDto.class);
    }
}
