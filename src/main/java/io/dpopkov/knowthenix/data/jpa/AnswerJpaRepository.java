package io.dpopkov.knowthenix.data.jpa;

import io.dpopkov.knowthenix.domain.entities.answer.AnswerEntity;
import io.dpopkov.knowthenix.domain.repositories.AnswerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerJpaRepository extends AnswerRepository, JpaRepository<AnswerEntity, Long> {

    @Override
    List<AnswerEntity> findAllByQuestionId(Long questionId);
}
