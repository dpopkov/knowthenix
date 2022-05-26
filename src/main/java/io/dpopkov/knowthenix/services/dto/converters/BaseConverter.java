package io.dpopkov.knowthenix.services.dto.converters;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;

public abstract class BaseConverter<S, T> implements Converter<S, T> {
    protected final ModelMapper mapper = new ModelMapper();
}
