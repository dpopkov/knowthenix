package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.domain.entities.user.Role;
import io.dpopkov.knowthenix.domain.repositories.AuthUserRepository;
import io.dpopkov.knowthenix.security.AuthUserPrincipal;
import io.dpopkov.knowthenix.services.AuthUserService;
import io.dpopkov.knowthenix.services.LoginAttemptService;
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
    private final LoginAttemptService loginAttemptService;

    public AuthUserServiceImpl(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService) {
        this.userRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AuthUserEntity> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            log.trace("{} {}", USER_NOT_FOUND_BY_USERNAME, username);
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME);
        }
        AuthUserEntity user = byUsername.get();
        log.trace("User found by username {}" ,username);
        updateLoginAttemptStatusFor(user);
        if (user.isNotLocked()) {
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
        }
        log.trace("User's notLocked status: {}", user.isNotLocked());
        userRepository.save(user);
        return new AuthUserPrincipal(user.getUsername(), user.getEncryptedPassword(),
                user.getAuthorities(), user.isNotLocked(), user.isActive());
    }

    private void updateLoginAttemptStatusFor(AuthUserEntity user) {
        if (user.isNotLocked()) {
            if (loginAttemptService.reachedAttemptsLimit(user.getUsername())) {
                log.info("Locking user '{}'", user.getUsername());
                user.setNotLocked(false);
            }
        } else {
            loginAttemptService.evictUser(user.getUsername());
        }
    }

    @Override
    public AuthUserEntity register(String firstName, String lastName, String username, String email)
            throws UsernameExistsException, EmailExistsException {
        validateNewUsernameAndEmail(username, email);
        AuthUserEntity newUser = AuthUserEntity.builder()
                .publicId(generatePublicId())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .encryptedPassword(encodePassword(generatePassword()))
                .email(email)
                .profileImageUrl(getTemporaryProfileImageUrl())
                .joinDate(new Date())
                .role(Role.defaultRole())
                .authorities(Role.defaultRole().getAuthoritiesAsList())
                .active(true)
                .notLocked(true)
                .build();
        AuthUserEntity savedUser = userRepository.save(newUser);
        log.trace("Saved registered user '{}'", savedUser.getUsername());
        return savedUser;
    }

    @Override
    public AuthUserEntity addNewUser(String firstName, String lastName, String username, String email, String role,
                               boolean isNotLocked, boolean isActive)
            throws EmailExistsException, UsernameExistsException {
        validateNewUsernameAndEmail(username, email);
        Role userRole = Role.valueOf(role.toUpperCase());
        AuthUserEntity newUser = AuthUserEntity.builder()
                .publicId(generatePublicId())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .encryptedPassword(encodePassword(generatePassword()))
                .email(email)
                .profileImageUrl(getTemporaryProfileImageUrl())
                .joinDate(new Date())
                .role(userRole)
                .authorities(userRole.getAuthoritiesAsList())
                .active(isActive)
                .notLocked(isNotLocked)
                .build();
        AuthUserEntity savedUser = userRepository.save(newUser);
        log.trace("Saved new user '{}'", savedUser.getUsername());
        return savedUser;
    }

    @Override
    public AuthUserEntity updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
                               String newEmail, String role, boolean isNotLocked, boolean isActive)
            throws UserNotFoundException, UsernameExistsException, EmailExistsException {
        AuthUserEntity user = validateUpdatingUsernameAndEmail(currentUsername, newUsername, newEmail);
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        user.setUsername(newUsername);
        user.setEmail(newEmail);
        Role userRole = Role.valueOf(role.toUpperCase());
        user.setRole(userRole);
        user.setAuthorities(userRole.getAuthoritiesAsList());
        user.setActive(isActive);
        user.setNotLocked(isNotLocked);
        AuthUserEntity savedUser = userRepository.save(user);
        log.trace("Saved updated user '{}'", savedUser.getUsername());
        return savedUser;
    }

    @Override
    public void deleteUserByUsername(String username) {
        // todo: implement archiving user instead of deleting
        userRepository.deleteByUsername(username);
        log.trace("User deleted by username {}", username);
    }

    private String generatePublicId() {
        return RandomStringUtils.randomNumeric(PUBLIC_ID_LENGTH);
    }

    private String generatePassword() {
        String password = RandomStringUtils.randomAlphanumeric(GENERATED_PASSWORD_LENGTH);
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

    private void validateNewUsernameAndEmail(String newUsername, String newEmail)
            throws UsernameExistsException, EmailExistsException {
        if (userRepository.findByUsername(newUsername).isPresent()) {
            throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
        }
    }

    private AuthUserEntity validateUpdatingUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
            throws UserNotFoundException, UsernameExistsException, EmailExistsException {
        var byCurrentUsername = userRepository.findByUsername(currentUsername);
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
        var byNewUsername = userRepository.findByUsername(newUsername);
        if (byNewUsername.isPresent() && currentUser.isNotSameById(byNewUsername.get())) {
            throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
        }
    }

    private void checkForOtherUserWithThisEmail(AuthUserEntity currentUser, String newEmail)
            throws EmailExistsException {
        var byNewEmail = userRepository.findByEmail(newEmail);
        if (byNewEmail.isPresent() && currentUser.isNotSameById(byNewEmail.get())) {
            throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public List<AuthUserEntity> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public AuthUserEntity findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
    }

    @Override
    public AuthUserEntity findByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_EMAIL));
    }
}
