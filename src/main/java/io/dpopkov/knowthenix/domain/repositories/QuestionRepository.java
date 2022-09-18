package io.dpopkov.knowthenix.domain.repositories;

import io.dpopkov.knowthenix.domain.entities.question.QuestionEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRepository extends BaseRepository<QuestionEntity> {

    List<QuestionEntity> findByCreatedOnBetween(LocalDateTime start, LocalDateTime end);
}
