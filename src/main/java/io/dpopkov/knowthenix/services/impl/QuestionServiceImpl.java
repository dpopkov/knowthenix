package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.domain.repositories.QuestionRepository;
import io.dpopkov.knowthenix.domain.repositories.QuestionTextRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.QuestionService;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import io.dpopkov.knowthenix.services.dto.converters.QuestionDtoToEntity;
import io.dpopkov.knowthenix.services.dto.converters.QuestionEntityToDto;
import io.dpopkov.knowthenix.services.dto.converters.QuestionTextEntityToDto;
import io.dpopkov.knowthenix.services.dto.converters.TranslationDtoToQuestionTextEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionTextRepository questionTextRepository;
    private final QuestionEntityToDto questionEntityToDto;
    private final QuestionDtoToEntity questionDtoToEntity;
    private final TranslationDtoToQuestionTextEntity translationDtoToQuestionTextEntity;
    private final QuestionTextEntityToDto questionTextEntityToDto;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionTextRepository questionTextRepository, QuestionEntityToDto questionEntityToDto, QuestionDtoToEntity questionDtoToEntity, TranslationDtoToQuestionTextEntity translationDtoToQuestionTextEntity, QuestionTextEntityToDto questionTextEntityToDto) {
        this.questionRepository = questionRepository;
        this.questionTextRepository = questionTextRepository;
        this.questionEntityToDto = questionEntityToDto;
        this.questionDtoToEntity = questionDtoToEntity;
        this.translationDtoToQuestionTextEntity = translationDtoToQuestionTextEntity;
        this.questionTextEntityToDto = questionTextEntityToDto;
    }

    @Override
    public QuestionDto create(QuestionDto dto) {
        // todo: write test and implement
        return null;
    }

    @Override
    public QuestionDto getById(Long id) {
        final QuestionEntity entity = questionRepository.findById(id)
                .orElseThrow(() -> new AppServiceException("Question not found"));
        return questionEntityToDto.convert(entity);
    }

    @Override
    public List<QuestionDto> getAll() {
        List<QuestionDto> result = new ArrayList<>();
        questionRepository.findAll().forEach(qe -> result.add(questionEntityToDto.convert(qe)));
        return result;
    }

    @Override
    public QuestionDto update(QuestionDto dto) {
        if (!questionRepository.existsById(dto.getId())) {
            throw new AppServiceException("Cannot find Question to update");
        }
        QuestionEntity entity = questionDtoToEntity.convert(dto);
        QuestionEntity saved = questionRepository.save(entity);
        return questionEntityToDto.convert(saved);
    }

    @Override
    public void delete(Long id) {
        // todo: write test and implement
    }

    @Transactional
    @Override
    public TranslationDto addTranslation(Long questionId, TranslationDto translation) {
        QuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppServiceException("Cannot find question by ID to add translation"));
        QuestionTextEntity entity = translationDtoToQuestionTextEntity.convert(translation);
        QuestionTextEntity savedEntity = questionTextRepository.save(entity);
        question.addTranslation(savedEntity);
        questionRepository.save(question);
        return questionTextEntityToDto.convert(savedEntity);
    }
}
