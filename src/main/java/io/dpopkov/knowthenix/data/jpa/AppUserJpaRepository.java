package io.dpopkov.knowthenix.data.jpa;

import io.dpopkov.knowthenix.domain.entities.user.AppUserEntity;
import io.dpopkov.knowthenix.domain.repositories.AppUserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserJpaRepository extends AppUserRepository, CrudRepository<AppUserEntity, Long> {
}
