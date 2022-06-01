package io.dpopkov.knowthenix.domain.repositories;

import io.dpopkov.knowthenix.domain.entities.BaseEntity;

import java.util.Optional;

public interface BaseRepository<T extends BaseEntity> {

    <S extends T> S save(S object);
    <S extends T> Iterable<S> saveAll(Iterable<S> objects);
    Iterable<T> findAll();
    Optional<T> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
    void delete(T object);
    void deleteAll();
    long count();
}
