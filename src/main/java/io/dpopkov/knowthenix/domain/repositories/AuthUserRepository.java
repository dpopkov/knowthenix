package io.dpopkov.knowthenix.domain.repositories;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;

import java.util.List;
import java.util.Optional;

public interface AuthUserRepository extends BaseRepository<AuthUserEntity> {

    Optional<AuthUserEntity> findByUsername(String username);

    Optional<AuthUserEntity> findByEmail(String email);

    List<AuthUserEntity> findAllByArchivedFalse();

    void deleteByUsername(String username);
}
