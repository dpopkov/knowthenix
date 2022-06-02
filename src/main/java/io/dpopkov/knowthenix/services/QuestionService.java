package io.dpopkov.knowthenix.services;

import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;

public interface QuestionService extends BaseService<QuestionDto> {

    TranslationDto addTranslation(Long questionId, TranslationDto translationDto);
}
