package io.dpopkov.knowthenix.services;

import io.dpopkov.knowthenix.services.dto.AppUserDto;

import java.util.List;

public interface AppUserService {

    AppUserDto getById(Long id);

    List<AppUserDto> getAll();
}
