package io.dpopkov.knowthenix.data.jpa;

import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.repositories.QuestionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionJpaRepository extends QuestionRepository, CrudRepository<QuestionEntity, Long> {
}
