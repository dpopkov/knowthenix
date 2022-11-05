package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.domain.repositories.AuthUserRepository;
import io.dpopkov.knowthenix.security.AuthUserPrincipal;
import io.dpopkov.knowthenix.services.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static io.dpopkov.knowthenix.security.SecurityMessages.USER_NOT_FOUND_BY_USERNAME;

@Slf4j
@Transactional
@Service
public class AuthUserServiceImpl implements AuthUserService, UserDetailsService {

    private final AuthUserRepository userRepository;

    public AuthUserServiceImpl(AuthUserRepository authUserRepository) {
        this.userRepository = authUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AuthUserEntity> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            log.trace("{} {}", USER_NOT_FOUND_BY_USERNAME, username);
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME);
        }
        AuthUserEntity user = byUsername.get();
        user.setLastLoginDateDisplay(user.getLastLoginDate());
        user.setLastLoginDate(new Date());
        userRepository.save(user);
        AuthUserPrincipal principal = new AuthUserPrincipal(user.getUsername(), user.getEncryptedPassword(),
                user.getAuthorities(), user.isNotLocked(), user.isActive());
        log.trace("User found by username {}" ,username);
        return principal;
    }
}
