package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.services.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static io.dpopkov.knowthenix.config.AppConstants.USERS_URL;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AppUserControllerTest {

    private static final Long ID = 12L;

    @Mock
    AppUserService service;
    @InjectMocks
    AppUserController controller;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllUsers() throws Exception {
        // When
        mockMvc.perform(get(USERS_URL))
                .andExpect(status().isOk());
        // Then
        then(service).should().getAll();
    }

    @Test
    void getUser() throws Exception {
        // When
        mockMvc.perform(get(USERS_URL + "/" + ID))
                .andExpect(status().isOk());
        // Then
        then(service).should().getById(ID);
    }
}
