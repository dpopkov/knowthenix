package io.dpopkov.knowthenix.services;

import io.dpopkov.knowthenix.services.dto.BaseDto;

import java.util.List;

public interface BaseService <T extends BaseDto> {

    T create(T dto);

    T getById(Long id);

    List<T> getAll();

    T update(T dto);

    void delete(Long id);
}
