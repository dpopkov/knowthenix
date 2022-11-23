package io.dpopkov.knowthenix.services.impl;

import io.dpopkov.knowthenix.domain.repositories.AppUserRepository;
import io.dpopkov.knowthenix.services.AppServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    private static final Long USER_ID = 12L;
    @Mock
    AppUserRepository appUserRepository;
    @InjectMocks
    AppUserServiceImpl service;

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
}
