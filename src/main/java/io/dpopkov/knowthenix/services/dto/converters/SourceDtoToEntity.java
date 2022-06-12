package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.services.dto.SourceDto;
import org.springframework.stereotype.Component;

@Component
public class SourceDtoToEntity extends BaseConverter<SourceDto, SourceEntity> {
    @Override
    public SourceEntity convert(SourceDto source) {
        return mapper.map(source, SourceEntity.class);
    }
}
