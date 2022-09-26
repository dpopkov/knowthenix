package io.dpopkov.knowthenix.domain.repositories;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;

import java.util.Optional;

public interface CategoryRepository extends BaseRepository<CategoryEntity> {

    Optional<CategoryEntity> findByName(String name);
}
