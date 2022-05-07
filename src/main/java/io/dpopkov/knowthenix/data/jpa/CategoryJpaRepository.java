package io.dpopkov.knowthenix.data.jpa;

import io.dpopkov.knowthenix.domain.entities.question.CategoryEntity;
import io.dpopkov.knowthenix.domain.repositories.CategoryRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpaRepository extends CategoryRepository, CrudRepository<CategoryEntity, Long> {
}
