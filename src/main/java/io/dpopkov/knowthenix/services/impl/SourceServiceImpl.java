package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.repositories.SourceRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.SourceService;
import io.dpopkov.knowthenix.services.dto.SourceDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;

    public SourceServiceImpl(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    @Override
    public SourceDto create(SourceDto dto) {
        ModelMapper mapper = new ModelMapper();
        SourceEntity entity = mapper.map(dto, SourceEntity.class);
        SourceEntity saved = sourceRepository.save(entity);
        return mapper.map(saved, SourceDto.class);
    }

    @Override
    public SourceDto getById(Long id) {
        SourceEntity entity = sourceRepository.findById(id)
                .orElseThrow(() -> new AppServiceException("Source not found"));
        return new ModelMapper().map(entity, SourceDto.class);
    }

    @Override
    public List<SourceDto> getAll() {
        Type typeOfList = new TypeToken<List<SourceDto>>() {
        }.getType();
        return new ModelMapper().map(sourceRepository.findAll(), typeOfList);
    }

    @Override
    public SourceDto update(SourceDto dto) {
        SourceEntity found = sourceRepository.findById(dto.getId()).orElseThrow(
                () -> new AppServiceException("Updated Source not found")
        );
        ModelMapper mapper = new ModelMapper();
        SourceEntity entity = mapper.map(dto, SourceEntity.class);
        found.copyFrom(entity);
        SourceEntity saved = sourceRepository.save(found);
        return mapper.map(saved, SourceDto.class);
    }

    @Override
    public void delete(Long id) {
        sourceRepository.deleteById(id);
    }
}
