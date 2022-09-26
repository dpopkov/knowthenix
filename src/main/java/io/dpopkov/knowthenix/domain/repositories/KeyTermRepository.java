package io.dpopkov.knowthenix.domain.repositories;

import io.dpopkov.knowthenix.domain.entities.KeyTermEntity;

import java.util.Optional;

public interface KeyTermRepository extends BaseRepository<KeyTermEntity> {

    Optional<KeyTermEntity> findByName(String name);
}
