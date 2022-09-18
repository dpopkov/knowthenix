package io.dpopkov.knowthenix.data.jpa;

import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;
import io.dpopkov.knowthenix.domain.repositories.QuestionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestionJpaRepository extends QuestionRepository, CrudRepository<QuestionEntity, Long> {

    List<QuestionEntity> findByCreatedOnBetween(LocalDateTime start, LocalDateTime end);
}
