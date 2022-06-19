package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerEntity;
import io.dpopkov.knowthenix.domain.entities.answer.AnswerTextEntity;
import io.dpopkov.knowthenix.services.dto.AnswerDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AnswerEntityToDto extends BaseConverter<AnswerEntity, AnswerDto> {

    private final AnswerTextEntityToDto answerTextEntityToDto;

    public AnswerEntityToDto(AnswerTextEntityToDto answerTextEntityToDto) {
        this.answerTextEntityToDto = answerTextEntityToDto;
    }

    @NonNull
    @Override
    public AnswerDto convert(AnswerEntity entity) {
        AnswerDto dto = new AnswerDto();
        dto.setId(entity.getId());
        dto.setQuestionId(entity.getQuestion().getId());
        dto.setSourceId(entity.getSource().getId());
        dto.setSourceName(entity.getSource().getName());
        dto.setSourceDetails(entity.getSourceDetails());
        dto.setSelectedLanguage(entity.getSelectedLanguage().name());
        for (AnswerTextEntity te : entity.getTranslations().values()) {
            TranslationDto translationDto = answerTextEntityToDto.convert(te);
            dto.addTranslation(translationDto);
        }
        return dto;
    }
}
