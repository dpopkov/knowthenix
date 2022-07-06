package io.dpopkov.knowthenix.services;

import io.dpopkov.knowthenix.services.dto.AnswerDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;

import java.util.List;

public interface AnswerService extends BaseService<AnswerDto> {

    List<AnswerDto> getAllForQuestion(Long questionId);

    List<TranslationDto> getTranslations(Long answerId);
}
