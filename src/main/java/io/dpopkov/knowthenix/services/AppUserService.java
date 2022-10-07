package io.dpopkov.knowthenix.services;

import io.dpopkov.knowthenix.services.dto.AppUserCreateDto;
import io.dpopkov.knowthenix.services.dto.AppUserDto;

import java.util.List;

public interface AppUserService {

    AppUserDto create(AppUserCreateDto dto);

    AppUserDto getById(Long id);

    List<AppUserDto> getAll();

    AppUserDto update(AppUserDto dto);

    void delete(Long id);

    void resetPassword(Long id);
}
