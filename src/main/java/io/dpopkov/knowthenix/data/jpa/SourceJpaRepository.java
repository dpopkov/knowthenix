package io.dpopkov.knowthenix.data.jpa;

import io.dpopkov.knowthenix.domain.entities.answer.SourceEntity;
import io.dpopkov.knowthenix.domain.repositories.SourceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceJpaRepository extends SourceRepository, JpaRepository<SourceEntity, Long> {
}
