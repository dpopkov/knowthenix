package io.dpopkov.knowthenix.data.jpa;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerTextEntity;
import io.dpopkov.knowthenix.domain.repositories.AnswerTextRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerTextJpaRepository extends AnswerTextRepository, JpaRepository<AnswerTextEntity, Long> {
}
