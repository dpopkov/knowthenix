package io.dpopkov.knowthenix.domain.repositories;

import io.dpopkov.knowthenix.domain.entities.user.AppUserEntity;

import java.util.Optional;

public interface AppUserRepository extends BaseRepository<AppUserEntity> {

    Optional<AppUserEntity> findByUsername(String username);
}
