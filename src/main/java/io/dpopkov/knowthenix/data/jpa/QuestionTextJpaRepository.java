package io.dpopkov.knowthenix.data.jpa;

import io.dpopkov.knowthenix.domain.entities.question.QuestionTextEntity;
import io.dpopkov.knowthenix.domain.repositories.QuestionTextRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTextJpaRepository extends QuestionTextRepository, JpaRepository<QuestionTextEntity, Long> {
}
