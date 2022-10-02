package io.dpopkov.knowthenix.rest.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.dpopkov.knowthenix.services.AppUserService;
import io.dpopkov.knowthenix.services.dto.AppUserCreateDto;
import io.dpopkov.knowthenix.services.dto.AppUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static io.dpopkov.knowthenix.config.AppConstants.USERS_URL;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AppUserControllerTest {

    private static final Long ID = 12L;
    private static final String NAME = "name1";
    private static final String NAME_UPD = "name2";
    private static final String PASSWORD = "password1";

    private final JsonMapper mapper = new JsonMapper();

    @Mock
    AppUserService service;
    @InjectMocks
    AppUserController controller;
    @Captor
    ArgumentCaptor<AppUserCreateDto> captorCreate;
    @Captor
    ArgumentCaptor<AppUserDto> captorUpdate;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createUser() throws Exception {
        // Given
        String postJson = mapper.writeValueAsString(new AppUserCreateDto(NAME, PASSWORD));
        AppUserDto returnDto = new AppUserDto(NAME);
        returnDto.setId(ID);
        given(service.create(any(AppUserCreateDto.class))).willReturn(returnDto);
        // When
        mockMvc.perform(post(USERS_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(postJson)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(ID.intValue())))
                .andExpect(jsonPath("$.name", is(NAME)));
        // Then
        then(service).should().create(captorCreate.capture());
        AppUserCreateDto captured = captorCreate.getValue();
        assertNull(captured.getId());
        assertEquals(NAME, captured.getName());
        assertEquals(PASSWORD, captured.getPassword());
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

    @Test
    void updateUser() throws Exception {
        // Given
        AppUserDto putDto = new AppUserDto(NAME_UPD);
        putDto.setId(ID);
        String putJson = mapper.writeValueAsString(putDto);
        AppUserDto returnDto = new AppUserDto(NAME_UPD);
        returnDto.setId(ID);
        given(service.update(any(AppUserDto.class))).willReturn(returnDto);
        // When
        mockMvc.perform(put(USERS_URL)
                .contentType(APPLICATION_JSON)
                .content(putJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(ID.intValue())))
                .andExpect(jsonPath("$.name", is(NAME_UPD)));
        // Then
        then(service).should().update(captorUpdate.capture());
        AppUserDto toUpdate = captorUpdate.getValue();
        assertEquals(ID, toUpdate.getId());
        assertEquals(NAME_UPD, toUpdate.getName());
    }

    @Test
    void deleteUser() throws Exception {
        // When
        mockMvc.perform(delete(USERS_URL + "/" + ID))
                .andExpect(status().isNoContent());
        // Then
        then(service).should().delete(ID);
    }
}
