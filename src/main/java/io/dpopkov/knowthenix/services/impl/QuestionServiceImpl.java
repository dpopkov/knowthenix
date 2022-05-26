package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.repositories.QuestionRepository;
import io.dpopkov.knowthenix.services.QuestionService;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.converters.QuestionEntityToDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionEntityToDto questionEntityToDto;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionEntityToDto questionEntityToDto) {
        this.questionRepository = questionRepository;
        this.questionEntityToDto = questionEntityToDto;
    }

    @Override
    public QuestionDto create(QuestionDto dto) {
        // todo: write test and implement
        return null;
    }

    @Override
    public QuestionDto getById(Long id) {
        // todo: write test and implement
        return null;
    }

    @Override
    public List<QuestionDto> getAll() {
        List<QuestionDto> result = new ArrayList<>();
        questionRepository.findAll().forEach(qe -> result.add(questionEntityToDto.convert(qe)));
        return result;
    }

    @Override
    public QuestionDto update(QuestionDto dto) {
        // todo: write test and implement
        return null;
    }

    @Override
    public void delete(Long id) {
        // todo: write test and implement
    }
}
