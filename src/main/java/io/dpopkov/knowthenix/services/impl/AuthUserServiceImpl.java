package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.config.FileConstants;
import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.domain.entities.user.Role;
import io.dpopkov.knowthenix.domain.repositories.AuthUserRepository;
import io.dpopkov.knowthenix.security.AuthUserPrincipal;
import io.dpopkov.knowthenix.services.AuthUserService;
import io.dpopkov.knowthenix.services.LoginAttemptService;
import io.dpopkov.knowthenix.services.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.dpopkov.knowthenix.security.SecurityMessages.USER_NOT_FOUND_BY_USERNAME;
import static io.dpopkov.knowthenix.services.impl.AuthUserServiceImplConstants.*;

@Slf4j
@Transactional
@Service
public class AuthUserServiceImpl implements AuthUserService, UserDetailsService {

    private static final String DIRECTORY_CREATED_FORMAT = "Created directory for: {}";
    private static final String FILE_SAVED_IN_FILE_SYSTEM_FORMAT = "File {} saved in file system as {}.";

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
                .profileImageUrl(getTemporaryProfileImageUrl(username))
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
                                     boolean isNotLocked, boolean isActive, MultipartFile profileImage)
            throws EmailExistsException, UsernameExistsException, IOException {
        validateNewUsernameAndEmail(username, email);
        Role userRole = Role.valueOf(role.toUpperCase());
        AuthUserEntity newUser = AuthUserEntity.builder()
                .publicId(generatePublicId())
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .encryptedPassword(encodePassword(generatePassword()))
                .email(email)
                .profileImageUrl(hasImage(profileImage) ? null : getTemporaryProfileImageUrl(username))
                .joinDate(new Date())
                .role(userRole)
                .authorities(userRole.getAuthoritiesAsList())
                .active(isActive)
                .notLocked(isNotLocked)
                .build();
        AuthUserEntity savedUser = userRepository.save(newUser);
        if (hasImage(profileImage)) {
            saveProfileImage(savedUser, profileImage);
        }
        log.trace("Saved new user '{}'", savedUser.getUsername());
        // todo: use email service to send notification by email
        return savedUser;
    }

    @Override
    public AuthUserEntity updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
                                     String newEmail, String role, boolean isNotLocked, boolean isActive,
                                     MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException {
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
        if (hasImage(profileImage)) {
            saveProfileImage(savedUser, profileImage);
        }
        log.trace("Saved updated user '{}'", savedUser.getUsername());
        return savedUser;
    }

    @Override
    public void deleteUserByUsername(String username) {
        // todo: implement archiving user instead of deleting
        userRepository.deleteByUsername(username);
        log.trace("User deleted by username {}", username);
    }

    @Override
    public AuthUserEntity updateProfileImage(String username, MultipartFile profileImage) {
        // todo: implement updating profile image
        throw new UnsupportedOperationException("Updating image is not implemented yet");
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

    private String getTemporaryProfileImageUrl(String username) {
        // todo: get rid of absolute url for temporary image, save only relative part
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DEFAULT_USER_IMAGE_PATH + username)
                .toUriString();
    }

    private String profileImageUrl(String username, String extension) {
        // todo: get rid of absolute url for real profile image, save only relative part
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(FileConstants.USER_IMAGE_PATH + username + "/" + username + "." + extension)
                .toUriString();
    }

    private void saveProfileImage(AuthUserEntity user, MultipartFile profileImage) throws IOException {
        if (hasImage(profileImage)) {
            String extension = getExtensionIfImage(Objects.requireNonNull(profileImage.getContentType()));
            if (extension == null) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            final String userImageFilename = user.getUsername() + "." + extension;
            final Path userFolder = ensureFolderForImage(user);
            final Path imagePath = userFolder.resolve(userImageFilename);
            // Replace with new image
            Files.deleteIfExists(imagePath);
            Files.copy(profileImage.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfileImageUrl(profileImageUrl(user.getUsername(), extension));
            userRepository.save(user);
            log.info(FILE_SAVED_IN_FILE_SYSTEM_FORMAT, profileImage.getOriginalFilename(), userImageFilename);
        }
    }

    private boolean hasImage(MultipartFile profileImage) {
        return profileImage != null && !profileImage.isEmpty();
    }

    private String getExtensionIfImage(String contentType) {
        switch (contentType) {
            case MediaType.IMAGE_JPEG_VALUE:
                return FileConstants.JPG_EXTENSION;
            case MediaType.IMAGE_PNG_VALUE:
                return FileConstants.PNG_EXTENSION;
            default:
                return null;
        }
    }

    private Path ensureFolderForImage(AuthUserEntity user) throws IOException {
        Path folder = Paths.get(FileConstants.USER_FOLDER, user.getUsername()).toAbsolutePath().normalize();
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
            log.info(DIRECTORY_CREATED_FORMAT, folder);
        }
        return folder;
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
