package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerEntity;
import io.dpopkov.knowthenix.domain.entities.answer.AnswerTextEntity;
import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.repositories.AnswerRepository;
import io.dpopkov.knowthenix.domain.repositories.SourceRepository;
import io.dpopkov.knowthenix.services.AnswerService;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.dto.AnswerDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import io.dpopkov.knowthenix.services.dto.converters.AnswerDtoToEntity;
import io.dpopkov.knowthenix.services.dto.converters.AnswerEntityToDto;
import io.dpopkov.knowthenix.services.dto.converters.AnswerTextEntityToDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.dpopkov.knowthenix.shared.Utils.*;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerDtoToEntity answerDtoToEntity;
    private final AnswerEntityToDto answerEntityToDto;
    private final AnswerTextEntityToDto answerTextEntityToDto;
    private final SourceRepository sourceRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, AnswerDtoToEntity answerDtoToEntity,
                             AnswerEntityToDto answerEntityToDto, AnswerTextEntityToDto answerTextEntityToDto, SourceRepository sourceRepository) {
        this.answerRepository = answerRepository;
        this.answerDtoToEntity = answerDtoToEntity;
        this.answerEntityToDto = answerEntityToDto;
        this.answerTextEntityToDto = answerTextEntityToDto;
        this.sourceRepository = sourceRepository;
    }

    @Override
    public List<AnswerDto> getAllForQuestion(Long questionId) {
        List<AnswerEntity> allByQuestion = answerRepository.findAllByQuestionId(questionId);
        List<AnswerDto> result = new ArrayList<>();
        allByQuestion.forEach(e -> result.add(answerEntityToDto.convert(e)));
        return result;
    }

    @Override
    public AnswerDto create(AnswerDto dto) {
        if (anyIdIsMissing(dto.getQuestionId(), dto.getSourceId())) {
            throw new AppServiceException(
                    "Some of IDs of associated Question or Source for the created Answer are missing");
        }
        AnswerEntity entity = answerDtoToEntity.convert(dto);
        SourceEntity sourceEntity = sourceRepository.findById(dto.getSourceId()).orElseThrow(
                () -> new AppServiceException("Cannot find Source by ID " + dto.getSourceId())
        );
        entity.setSource(sourceEntity);
        AnswerEntity saved = answerRepository.save(entity);
        AnswerDto converted = answerEntityToDto.convert(saved);
        converted.setSourceName(sourceEntity.getName());
        return converted;
    }

    @Override
    public AnswerDto getById(Long id) {
        AnswerEntity found = answerRepository.findById(id).orElseThrow(
                () -> new AppServiceException("Cannot find Answer by ID " + id));
        return answerEntityToDto.convert(found);
    }

    @Override
    public List<AnswerDto> getAll() {
        return null;
    }

    @Override
    public AnswerDto update(AnswerDto dto) {
        if (anyIdIsMissing(dto.getQuestionId(), dto.getSourceId())) {
            throw new AppServiceException(
                    "Some of IDs of associated Question or Source for the updated Answer are missing");
        }
        AnswerEntity foundEntity = answerRepository.findById(dto.getId()).orElseThrow(
                () -> new AppServiceException("Cannot find Answer by ID " + dto.getId()));
        if (!foundEntity.getSource().getId().equals(dto.getSourceId())) {
            SourceEntity updatedSource = sourceRepository.findById(dto.getSourceId()).orElseThrow(
                    () -> new AppServiceException("Cannot find Source by ID " + dto.getSourceId()));
            foundEntity.setSource(updatedSource);
        }
        AnswerEntity draftEntity = answerDtoToEntity.convert(dto);
        foundEntity.setSourceDetails(draftEntity.getSourceDetails());
        foundEntity.setSelectedLanguage(draftEntity.getSelectedLanguage());
        foundEntity.setTranslations(draftEntity.getTranslations());
        AnswerEntity saved = answerRepository.save(foundEntity);
        return answerEntityToDto.convert(saved);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<TranslationDto> getTranslations(Long answerId) {
        AnswerEntity answerEntity = answerRepository.findById(answerId).orElseThrow();
        final Collection<AnswerTextEntity> values = answerEntity.getTranslations().values();
        List<TranslationDto> result = new ArrayList<>();
        values.forEach(textEntity -> result.add(answerTextEntityToDto.convert(textEntity)));
        return result;
    }
}
