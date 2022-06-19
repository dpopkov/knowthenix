package io.dpopkov.knowthenix.domain.repositories;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerEntity;

import java.util.List;

public interface AnswerRepository extends BaseRepository<AnswerEntity> {

    List<AnswerEntity> findAllByQuestionId(Long questionId);
}
