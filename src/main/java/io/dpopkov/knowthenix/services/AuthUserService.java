package io.dpopkov.knowthenix.services;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.services.exceptions.*;

import java.util.List;
import java.util.Optional;

public interface AuthUserService {

    AuthUserEntity register(String firstName, String lastName, String username, String email)
            throws UsernameExistsException, EmailExistsException;

    List<AuthUserEntity> getAllUsers();

    Optional<AuthUserEntity> findByUsername(String username);

    Optional<AuthUserEntity> findByEmail(String email);
}
