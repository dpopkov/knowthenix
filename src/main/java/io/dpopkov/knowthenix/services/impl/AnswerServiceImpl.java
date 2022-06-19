package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerEntity;
import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.repositories.AnswerRepository;
import io.dpopkov.knowthenix.domain.repositories.SourceRepository;
import io.dpopkov.knowthenix.services.AnswerService;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.dto.AnswerDto;
import io.dpopkov.knowthenix.services.dto.converters.AnswerDtoToEntity;
import io.dpopkov.knowthenix.services.dto.converters.AnswerEntityToDto;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.dpopkov.knowthenix.shared.Utils.*;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerDtoToEntity answerDtoToEntity;
    private final AnswerEntityToDto answerEntityToDto;
    private final SourceRepository sourceRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, AnswerDtoToEntity answerDtoToEntity, AnswerEntityToDto answerEntityToDto, SourceRepository sourceRepository) {
        this.answerRepository = answerRepository;
        this.answerDtoToEntity = answerDtoToEntity;
        this.answerEntityToDto = answerEntityToDto;
        this.sourceRepository = sourceRepository;
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
        return null;
    }

    @Override
    public List<AnswerDto> getAll() {
        return null;
    }

    @Override
    public AnswerDto update(AnswerDto dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
