package io.dpopkov.knowthenix.services.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionDto extends BaseDto {
    private CategoryDto category;
    private String selectedLanguage;
    private String createdAt;
    private List<TranslationDto> translations = new ArrayList<>();

    public void addTranslation(TranslationDto translationDto) {
        translations.add(translationDto);
    }
}
