package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.user.AppUserEntity;
import io.dpopkov.knowthenix.domain.repositories.AppUserRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.AppUserService;
import io.dpopkov.knowthenix.services.dto.AppUserCreateDto;
import io.dpopkov.knowthenix.services.dto.AppUserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUserDto create(AppUserCreateDto dto) {
        AppUserEntity entity = dto.asUserEntity();
        AppUserEntity created = appUserRepository.save(entity);
        return AppUserDto.from(created);
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public AppUserDto update(AppUserDto dto) {
        Optional<AppUserEntity> byId = appUserRepository.findById(dto.getId());
        if (byId.isEmpty()) {
            throw new AppServiceException("Updated User not found");
        }
        AppUserEntity entity = byId.get();
        entity.setName(dto.getName());
        AppUserEntity saved = appUserRepository.save(entity);
        return AppUserDto.from(saved);
    }

    @Override
    public void delete(Long id) {
        appUserRepository.deleteById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void resetPassword(Long id) {
        Optional<AppUserEntity> byId = appUserRepository.findById(id);
        if (byId.isEmpty()) {
            throw new AppServiceException("User not found");
        }
        AppUserEntity entity = byId.get();
        entity.setPassword("secret");
        appUserRepository.save(entity);
    }
}
