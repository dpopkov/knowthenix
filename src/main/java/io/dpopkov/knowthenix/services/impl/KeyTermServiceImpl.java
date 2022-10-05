package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.KeyTermEntity;
import io.dpopkov.knowthenix.domain.repositories.KeyTermRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.KeyTermService;
import io.dpopkov.knowthenix.services.dto.KeyTermDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class KeyTermServiceImpl implements KeyTermService {

    private final KeyTermRepository keyTermRepository;

    public KeyTermServiceImpl(KeyTermRepository keyTermRepository) {
        this.keyTermRepository = keyTermRepository;
    }

    @Override
    public KeyTermDto create(KeyTermDto dto) {
        ModelMapper mapper = new ModelMapper();
        KeyTermEntity entity = mapper.map(dto, KeyTermEntity.class);
        KeyTermEntity created = keyTermRepository.save(entity);
        return mapper.map(created, KeyTermDto.class);
    }

    @Override
    public KeyTermDto getById(Long id) {
        KeyTermEntity entity = keyTermRepository.findById(id)
                .orElseThrow(() -> new AppServiceException("KeyTerm not found"));
        return new ModelMapper().map(entity, KeyTermDto.class);
    }

    @Override
    public List<KeyTermDto> getAll() {
        Type typeOfList = new TypeToken<List<KeyTermDto>>() {}.getType();
        return new ModelMapper().map(keyTermRepository.findAll(), typeOfList);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public KeyTermDto update(KeyTermDto dto) {
        Optional<KeyTermEntity> byId = keyTermRepository.findById(dto.getId());
        if (byId.isEmpty()) {
            throw new AppServiceException("Updated KeyTerm not found");
        }
        KeyTermEntity entity = byId.get();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        KeyTermEntity saved = keyTermRepository.save(entity);
        return new ModelMapper().map(saved, KeyTermDto.class);
    }

    @Override
    public void delete(Long id) {
        keyTermRepository.deleteById(id);
    }
}
