package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.domain.entities.user.Role;
import io.dpopkov.knowthenix.domain.repositories.AuthUserRepository;
import io.dpopkov.knowthenix.services.LoginAttemptService;
import io.dpopkov.knowthenix.services.dto.AuthUserDto;
import io.dpopkov.knowthenix.services.dto.converters.AuthUserEntityToDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUserServiceImplTest {

    private static final String FIRST_NAME = "first-name";
    private static final String LAST_NAME = "last-name";
    private static final String EMAIL = "user@example.org";
    private static final String USERNAME = "name";
    private static final String NEW_USERNAME = "new-name";
    private static final String PASSWORD = "123";
    private static final Collection<String> AUTHORITIES_COLLECTION = List.of("auth1", "auth2");

    @Mock
    AuthUserRepository authUserRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    LoginAttemptService loginAttemptService;
    AuthUserEntityToDto authUserEntityToDto;
    @InjectMocks
    AuthUserServiceImpl service;
    @Captor
    ArgumentCaptor<AuthUserEntity> captor;

    @BeforeEach
    void setUp() {
        authUserEntityToDto = new AuthUserEntityToDto();
        service = new AuthUserServiceImpl(authUserRepository, passwordEncoder, loginAttemptService, authUserEntityToDto);
    }

    @Test
    void loadUserByUsername() {
        // Given
        AuthUserEntity entity = new AuthUserEntity();
        entity.setUsername(USERNAME);
        entity.setEncryptedPassword(PASSWORD);
        entity.setAuthorities(AUTHORITIES_COLLECTION);
        entity.setNotLocked(false);
        given(authUserRepository.findByUsername(USERNAME)).willReturn(Optional.of(entity));
        // When
        final UserDetails userDetails = service.loadUserByUsername(USERNAME);
        // Then
        assertNotNull(userDetails);
        assertEquals(USERNAME, userDetails.getUsername());
        assertEquals(PASSWORD, userDetails.getPassword());
        List<String> authorities = userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        assertEquals(AUTHORITIES_COLLECTION, authorities);
        then(authUserRepository).should().findByUsername(USERNAME);
        then(authUserRepository).should().save(captor.capture());
        AuthUserEntity value = captor.getValue();
        assertEquals(USERNAME, value.getUsername());
        assertEquals(PASSWORD, value.getEncryptedPassword());
        then(loginAttemptService).should().evictUser(USERNAME);
    }

    @Test
    void registerWithRole() {
        // Given
        Role role = Role.ROLE_USER;
        AuthUserEntity entity = new AuthUserEntity();
        entity.setFirstName(FIRST_NAME);
        entity.setLastName(LAST_NAME);
        entity.setUsername(USERNAME);
        entity.setEmail(EMAIL);
        entity.setAuthorities(role.getAuthoritiesAsList());
        given(authUserRepository.save(any(AuthUserEntity.class))).willReturn(entity);
        // When
        final AuthUserDto dto = service.registerWithRole(FIRST_NAME, LAST_NAME, USERNAME, EMAIL, role, PASSWORD);
        // Then
        assertNotNull(dto);
        assertEquals(FIRST_NAME, dto.getFirstName());
        assertEquals(LAST_NAME, dto.getLastName());
        assertEquals(USERNAME, dto.getUsername());
        assertEquals(EMAIL, dto.getEmail());
        assertEquals(role.getAuthoritiesAsList(), dto.getAuthorities());
        then(authUserRepository).should().save(captor.capture());
        AuthUserEntity value = captor.getValue();
        assertEquals(FIRST_NAME, value.getFirstName());
        assertEquals(LAST_NAME, value.getLastName());
        assertEquals(USERNAME, value.getUsername());
        assertEquals(EMAIL, value.getEmail());
        assertEquals(role.getAuthoritiesAsList(), value.getAuthorities());
    }

    @Test
    void addNewUser() throws IOException {
        // Given
        Role role = Role.ROLE_USER;
        MultipartFile profileImage = new MockMultipartFile("profile", new byte[0]);
        AuthUserEntity saved = new AuthUserEntity();
        saved.setUsername(USERNAME);
        given(authUserRepository.save(any())).willReturn(saved);
        service.setAbsoluteProfileImagePrefix("test-prefix");
        // When
        final AuthUserDto added = service.addNewUser(FIRST_NAME, LAST_NAME, USERNAME, EMAIL, role.name(),
                true, true, profileImage);
        // Then
        assertNotNull(added);
        assertEquals(USERNAME, added.getUsername());
        then(authUserRepository).should().findByUsername(USERNAME);
        then(authUserRepository).should().findByEmail(EMAIL);
        then(authUserRepository).should().save(captor.capture());
        AuthUserEntity captured = captor.getValue();
        assertEquals(FIRST_NAME, captured.getFirstName());
        assertEquals(LAST_NAME, captured.getLastName());
        assertEquals(USERNAME, captured.getUsername());
        assertEquals(role, captured.getRole());
        assertNotNull(captured.getPublicId());
        assertNotNull(captured.getProfileImageUrl());
        assertNotNull(captured.getJoinDate());
        assertNotNull(captured.getAppUserEntity());
    }

    @Test
    void updateUser() throws IOException {
        // Given
        Role role = Role.ROLE_USER;
        MultipartFile profileImage = new MockMultipartFile("profile", new byte[0]);

        AuthUserEntity userEntity = new AuthUserEntity();
        userEntity.setUsername(USERNAME);
        given(authUserRepository.findByUsername(USERNAME)).willReturn(Optional.of(userEntity));
        given(authUserRepository.save(any())).willReturn(userEntity);
        service.setAbsoluteProfileImagePrefix("test-prefix");
        // When
        final AuthUserDto updated = service.updateUser(USERNAME, FIRST_NAME, LAST_NAME, NEW_USERNAME, EMAIL,
                role.name(), true, true, profileImage);
        // Then
        assertNotNull(updated);
        assertEquals(NEW_USERNAME, updated.getUsername());
        then(authUserRepository).should().findByUsername(USERNAME);
        then(authUserRepository).should().findByEmail(EMAIL);
        then(authUserRepository).should().save(captor.capture());
        AuthUserEntity captured = captor.getValue();
        assertEquals(FIRST_NAME, captured.getFirstName());
        assertEquals(LAST_NAME, captured.getLastName());
        assertEquals(NEW_USERNAME, captured.getUsername());
        assertEquals(role, captured.getRole());
    }

    @Test
    void deleteUserByUsername() {
        // Given
        final AuthUserEntity found = new AuthUserEntity();
        found.setUsername(USERNAME);
        found.setArchived(false);
        found.setNotLocked(true);
        found.setActive(true);
        given(authUserRepository.findByUsername(USERNAME)).willReturn(Optional.of(found));
        // When
        service.deleteUserByUsername(USERNAME);
        // Then
        then(authUserRepository).should().save(captor.capture());
        final AuthUserEntity archived = captor.getValue();
        assertEquals(USERNAME, archived.getUsername());
        assertTrue(archived.isArchived());
        assertFalse(archived.isNotLocked());
        assertFalse(archived.isActive());
    }

    @Test
    void resetPassword() {
        // Given
        AuthUserEntity old = new AuthUserEntity();
        old.setEncryptedPassword(PASSWORD);
        given(authUserRepository.findByEmail(EMAIL)).willReturn(Optional.of(old));
        // When
        service.resetPassword(EMAIL);
        // Then
        then(authUserRepository).should().findByEmail(EMAIL);
        then(authUserRepository).should().save(captor.capture());
        final AuthUserEntity saved = captor.getValue();
        assertNotEquals(PASSWORD, saved.getEncryptedPassword());
    }
}
