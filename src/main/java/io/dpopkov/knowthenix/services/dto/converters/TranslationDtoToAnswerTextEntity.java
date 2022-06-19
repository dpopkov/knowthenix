package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerTextEntity;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.springframework.stereotype.Component;

@Component
public class TranslationDtoToAnswerTextEntity extends BaseConverter<TranslationDto, AnswerTextEntity> {
    @Override
    public AnswerTextEntity convert(TranslationDto dto) {
        return mapper.map(dto, AnswerTextEntity.class);
    }
}
