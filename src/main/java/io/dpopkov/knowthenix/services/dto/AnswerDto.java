package io.dpopkov.knowthenix.services.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AnswerDto extends BaseDto {

    private Long questionId;
    private Long sourceId;
    private String sourceName; // do not map it to entity
    private String sourceDetails;
    private String selectedLanguage;
    private List<TranslationDto> translations = new ArrayList<>();

    public void addTranslation(TranslationDto translationDto) {
        translations.add(translationDto);
    }
}
