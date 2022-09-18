package io.dpopkov.knowthenix.services;

import io.dpopkov.knowthenix.services.dto.IdChangeSetDto;
import io.dpopkov.knowthenix.services.dto.KeyTermDto;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;

import java.util.Collection;
import java.util.List;

public interface QuestionService extends BaseService<QuestionDto> {

    List<TranslationDto> getTranslations(Long questionId);

    TranslationDto addTranslation(Long questionId, TranslationDto translationDto);

    TranslationDto updateTranslation(Long questionId, TranslationDto translationDto);

    Collection<KeyTermDto> getKeyTermsByQuestionId(Long questionId);

    /** Patches set of Questions's keyterms and returns collection of IDs of updated Question. */
    Collection<Long> changeKeyTermsByQuestionId(Long questionId, IdChangeSetDto idChangeSetDto);

    List<QuestionDto> getCreatedOn(String createdOn);
}
