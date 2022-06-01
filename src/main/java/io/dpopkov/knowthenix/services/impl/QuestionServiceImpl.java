package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.repositories.QuestionRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.QuestionService;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.converters.QuestionDtoToEntity;
import io.dpopkov.knowthenix.services.dto.converters.QuestionEntityToDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionEntityToDto questionEntityToDto;
    private final QuestionDtoToEntity questionDtoToEntity;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionEntityToDto questionEntityToDto, QuestionDtoToEntity questionDtoToEntity) {
        this.questionRepository = questionRepository;
        this.questionEntityToDto = questionEntityToDto;
        this.questionDtoToEntity = questionDtoToEntity;
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
}
