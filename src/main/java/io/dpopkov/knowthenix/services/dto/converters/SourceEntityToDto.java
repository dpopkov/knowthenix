package io.dpopkov.knowthenix.services.dto.converters;

import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.services.dto.SourceDto;
import org.springframework.stereotype.Component;

@Component
public class SourceEntityToDto extends BaseConverter<SourceEntity, SourceDto> {
    @Override
    public SourceDto convert(SourceEntity source) {
        return mapper.map(source, SourceDto.class);
    }
}
