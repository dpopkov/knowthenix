package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.domain.entities.user.Role;
import io.dpopkov.knowthenix.domain.repositories.AuthUserRepository;
import io.dpopkov.knowthenix.security.AuthUserPrincipal;
import io.dpopkov.knowthenix.services.AuthUserService;
import io.dpopkov.knowthenix.services.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.dpopkov.knowthenix.security.SecurityMessages.USER_NOT_FOUND_BY_USERNAME;
import static io.dpopkov.knowthenix.services.impl.AuthUserServiceImplConstants.*;

@Slf4j
@Transactional
@Service
public class AuthUserServiceImpl implements AuthUserService, UserDetailsService {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthUserServiceImpl(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public AuthUserEntity register(String firstName, String lastName, String username, String email)
            throws UsernameExistsException, EmailExistsException {
        validateRegisteringUsernameAndEmail(username, email);
        AuthUserEntity newUser = AuthUserEntity.builder()
                .publicId(generatePublicId())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .encryptedPassword(encodePassword(generatePassword()))
                .email(email)
                .profileImageUrl(getTemporaryProfileImageUrl())
                .joinDate(new Date())
                .role(Role.ROLE_USER)
                .authorities(Role.ROLE_USER.getAuthoritiesAsList())
                .active(true)
                .notLocked(true)
                .build();
        AuthUserEntity savedUser = userRepository.save(newUser);
        log.trace("Saved user '{}'", savedUser.getUsername());
        return savedUser;
    }

    private String generatePublicId() {
        return RandomStringUtils.randomNumeric(16);
    }

    private String generatePassword() {
        String password = RandomStringUtils.randomAlphanumeric(16);
        // todo: remove logging of generated password when the complete functionality is ready.
        log.info("Generated password: {}", password);
        return password;
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private String getTemporaryProfileImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DEFAULT_USER_IMAGE_PATH + "/temp")
                .toUriString();
    }

    private void validateRegisteringUsernameAndEmail(String newUsername, String newEmail)
            throws UsernameExistsException, EmailExistsException {
        if (findByUsername(newUsername).isPresent()) {
            throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        }
        if (findByEmail(newEmail).isPresent()) {
            throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
        }
    }

    private AuthUserEntity validateUpdatingUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
            throws UserNotFoundException, UsernameExistsException, EmailExistsException {
        var byCurrentUsername = findByUsername(currentUsername);
        if (byCurrentUsername.isEmpty()) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
        }
        AuthUserEntity currentUser = byCurrentUsername.get();
        checkForOtherUserWithThisUsername(currentUser, newUsername);
        checkForOtherUserWithThisEmail(currentUser, newEmail);
        return currentUser;
    }

    private void checkForOtherUserWithThisUsername(AuthUserEntity currentUser, String newUsername)
            throws UsernameExistsException {
        var byNewUsername = findByUsername(newUsername);
        if (byNewUsername.isPresent() && currentUser.isNotSameById(byNewUsername.get())) {
            throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        }
    }

    private void checkForOtherUserWithThisEmail(AuthUserEntity currentUser, String newEmail)
            throws EmailExistsException {
        var byByNewEmail = findByEmail(newEmail);
        if (byByNewEmail.isPresent() && currentUser.isNotSameById(byByNewEmail.get())) {
            throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public List<AuthUserEntity> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<AuthUserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
