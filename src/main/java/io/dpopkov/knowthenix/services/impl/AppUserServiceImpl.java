package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.user.AppUserEntity;
import io.dpopkov.knowthenix.domain.repositories.AppUserRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.AppUserService;
import io.dpopkov.knowthenix.services.dto.AppUserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUserDto getById(Long id) {
        AppUserEntity found = appUserRepository.findById(id)
                .orElseThrow(() -> new AppServiceException("User not found"));
        return AppUserDto.from(found);
    }

    @Override
    public List<AppUserDto> getAll() {
        Iterable<AppUserEntity> all = appUserRepository.findAll();
        List<AppUserDto> list = new ArrayList<>();
        all.forEach(e -> list.add(AppUserDto.from(e)));
        return list;
    }
}
