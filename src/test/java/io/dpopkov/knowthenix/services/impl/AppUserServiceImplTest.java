package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.entities.user.AppUserEntity;
import io.dpopkov.knowthenix.domain.repositories.AppUserRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.dto.AppUserCreateDto;
import io.dpopkov.knowthenix.services.dto.AppUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    private static final Long USER_ID = 12L;
    private static final String USER_NAME = "user1";
    private static final String USER_NAME_UPD = "user2";
    private static final String USER_PASSWORD = "password1";
    @Mock
    AppUserRepository appUserRepository;
    @InjectMocks
    AppUserServiceImpl service;
    @Captor
    ArgumentCaptor<AppUserEntity> captor;

    private AppUserEntity returnEntity;

    @BeforeEach
    void setUp() {
        returnEntity = new AppUserEntity();
        returnEntity.setId(USER_ID);
        returnEntity.setName(USER_NAME);
        returnEntity.setPassword(USER_PASSWORD);
    }

    @Test
    void create() {
        // Given
        AppUserCreateDto dto = new AppUserCreateDto(USER_NAME, USER_PASSWORD);
        given(appUserRepository.save(any(AppUserEntity.class))).willReturn(returnEntity);
        // When
        AppUserDto createdDto = service.create(dto);
        // Then
        assertNotNull(createdDto);
        assertEquals(USER_ID, createdDto.getId());
        assertEquals(USER_NAME, createdDto.getName());
        then(appUserRepository).should().save(captor.capture());
        AppUserEntity entity = captor.getValue();
        assertNull(entity.getId());
        assertEquals(USER_NAME, entity.getName());
        assertEquals(USER_PASSWORD, entity.getPassword());
    }

    @Test
    void getById() {
        // Given
        given(appUserRepository.findById(USER_ID)).willReturn(Optional.of(returnEntity));
        // When
        AppUserDto found = service.getById(USER_ID);
        // Then
        assertNotNull(found);
        assertEquals(USER_ID, found.getId());
        assertEquals(USER_NAME, found.getName());
        then(appUserRepository).should().findById(USER_ID);
    }

    @Test
    void getById_whenNotFound_thenException() {
        // Given
        given(appUserRepository.findById(USER_ID)).willReturn(Optional.empty());
        // When
        assertThrows(AppServiceException.class, () -> service.getById(USER_ID));
        // Then
        then(appUserRepository).should().findById(USER_ID);
        then(appUserRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void getAll() {
        // Given
        given(appUserRepository.findAll()).willReturn(List.of(returnEntity));
        // When
        List<AppUserDto> all = service.getAll();
        // Then
        assertNotNull(all);
        assertEquals(1, all.size());
        assertEquals(USER_ID, all.get(0).getId());
        then(appUserRepository).should().findAll();
    }

    @Test
    void update() {
        // Given
        AppUserDto toUpdate = new AppUserDto(USER_NAME_UPD);
        toUpdate.setId(USER_ID);
        AppUserEntity oldUser = new AppUserEntity();
        oldUser.setId(USER_ID);
        oldUser.setName(USER_NAME);
        given(appUserRepository.findById(USER_ID)).willReturn(Optional.of(oldUser));
        AppUserEntity updatedEntity = new AppUserEntity();
        updatedEntity.setId(USER_ID);
        updatedEntity.setName(USER_NAME_UPD);
        given(appUserRepository.save(any(AppUserEntity.class))).willReturn(updatedEntity);
        // When
        AppUserDto updated = service.update(toUpdate);
        // Then
        assertNotNull(updated);
        assertEquals(USER_ID, updated.getId());
        assertEquals(USER_NAME_UPD, updated.getName());
        then(appUserRepository).should().findById(USER_ID);
        then(appUserRepository).should().save(captor.capture());
        AppUserEntity captured = captor.getValue();
        assertEquals(USER_ID, captured.getId());
        assertEquals(USER_NAME_UPD, captured.getName());
    }

    @Test
    void update_whenNotFound_thenException() {
        // Given
        AppUserDto toUpdate = new AppUserDto(USER_NAME_UPD);
        toUpdate.setId(USER_ID);
        given(appUserRepository.findById(USER_ID)).willReturn(Optional.empty());
        // When
        assertThrows(AppServiceException.class, () -> service.update(toUpdate));
        // Then
        then(appUserRepository).should().findById(USER_ID);
        then(appUserRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void delete() {
        // When
        service.delete(USER_ID);
        // Then
        then(appUserRepository).should().deleteById(USER_ID);
    }
}
