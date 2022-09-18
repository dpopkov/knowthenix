package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class QuestionEntityToDto implements Converter<QuestionEntity, QuestionDto> {

    private final CategoryEntityToDto categoryEntityToDto;
    private final QuestionTextEntityToDto questionTextEntityToDto;

    public QuestionEntityToDto(CategoryEntityToDto categoryEntityToDto, QuestionTextEntityToDto questionTextEntityToDto) {
        this.categoryEntityToDto = categoryEntityToDto;
        this.questionTextEntityToDto = questionTextEntityToDto;
    }

    @Override
    public QuestionDto convert(QuestionEntity entity) {
        QuestionDto dto = new QuestionDto();
        dto.setId(entity.getId());
        if (entity.getCategory() != null) {
            dto.setCategory(categoryEntityToDto.convert(entity.getCategory()));
        }
        if (entity.getSelectedLanguage() != null) {
            dto.setSelectedLanguage(entity.getSelectedLanguage().name());
        }
        if (entity.getCreatedOn() != null) {
            dto.setCreatedAt(entity.getCreatedOn().toLocalDate().toString());
        }
        if (entity.getTranslations() != null && !entity.getTranslations().isEmpty()) {
            for (QuestionTextEntity qt : entity.getTranslations().values()) {
                dto.addTranslation(questionTextEntityToDto.convert(qt));
            }
        }
        return dto;
    }
}
