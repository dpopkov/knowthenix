package io.dpopkov.knowthenix.rest.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.domain.entities.user.Role;
import io.dpopkov.knowthenix.rest.model.request.LoginUserRequest;
import io.dpopkov.knowthenix.rest.model.request.RegisterUserRequest;
import io.dpopkov.knowthenix.security.JwtProvider;
import io.dpopkov.knowthenix.security.SecurityConstants;
import io.dpopkov.knowthenix.services.AuthUserService;
import io.dpopkov.knowthenix.services.TemporaryProfileImagesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static io.dpopkov.knowthenix.config.AppConstants.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthUserControllerTest {
    private static final String FIRST_NAME = "first-name";
    private static final String LAST_NAME = "last-name";
    private static final String EMAIL = "user@example.org";
    private static final String USERNAME = "name";
    private static final String ROLE = Role.defaultRole().name();
    private static final String NEW_USERNAME = "new-name";
    private static final String PASSWORD = "123";

    @Mock
    AuthUserService authUserService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtProvider jwtProvider;
    @Mock
    TemporaryProfileImagesService temporaryProfileImagesService;
    @InjectMocks
    AuthUserController controller;
    MockMvc mockMvc;
    final JsonMapper mapper = new JsonMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void register() throws Exception {
        // Given
        RegisterUserRequest request = new RegisterUserRequest();
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setUsername(USERNAME);
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        String postJson = mapper.writeValueAsString(request);
        // When
        mockMvc.perform(post(USER_URL + REGISTER_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(postJson)
        )
                .andExpect(status().isCreated());
        // Then
        then(authUserService).should().register(FIRST_NAME, LAST_NAME, USERNAME, EMAIL, PASSWORD);
    }

    @Test
    void login() throws Exception {
        // Given
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername(USERNAME);
        request.setPassword(PASSWORD);
        String postJson = mapper.writeValueAsString(request);
        given(authUserService.findEntityByUsername(USERNAME)).willReturn(new AuthUserEntity());
        given(jwtProvider.generateToken(any())).willReturn("token");
        // When
        mockMvc.perform(post(USER_URL + LOGIN_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(postJson)
        )
                .andExpect(status().isOk())
                .andExpect(header().exists(SecurityConstants.JWT_HEADER));
        // Then
        then(authUserService).should().findEntityByUsername(USERNAME);
        then(jwtProvider).should().generateToken(any(UserDetails.class));
        then(authUserService).should().convert(any(AuthUserEntity.class));
    }

    @Test
    void addNewUser() throws Exception {
        // When
        mockMvc.perform(post(USER_URL)
                    .param("firstName", FIRST_NAME)
                    .param("lastName", LAST_NAME)
                    .param("username", USERNAME)
                    .param("email", EMAIL)
                    .param("role", ROLE)
                    .param("notLocked", "true")
                    .param("active", "true")
        )
                .andExpect(status().isCreated());
        // Then
        then(authUserService).should()
                .addNewUser(FIRST_NAME, LAST_NAME, USERNAME, EMAIL, ROLE, true, true, null);
    }

    @Test
    void updateUser() throws Exception {
        // When
        mockMvc.perform(put(USER_URL)
                    .param("currentUsername", USERNAME)
                    .param("firstName", FIRST_NAME)
                    .param("lastName", LAST_NAME)
                    .param("username", NEW_USERNAME)
                    .param("email", EMAIL)
                    .param("role", ROLE)
                    .param("notLocked", "true")
                    .param("active", "true")
        )
                .andExpect(status().isOk());
        // Then
        then(authUserService).should()
                .updateUser(USERNAME, FIRST_NAME, LAST_NAME, NEW_USERNAME, EMAIL, ROLE,
                        true, true, null);
    }

    @Test
    void getByUsername() throws Exception {
        // When
        mockMvc.perform(get(USER_URL + "/{username}", USERNAME))
                .andExpect(status().isOk());
        // Then
        then(authUserService).should().findByUsername(USERNAME);
    }

    @Test
    void getAllUsers() throws Exception {
        // When
        mockMvc.perform(get(USER_URL)).andExpect(status().isOk());
        // Then
        then(authUserService).should().getAllUsers();
    }

    @Test
    void resetPassword() throws Exception {
        // When
        mockMvc.perform(put(USER_URL + RESET_PASSWORD_URL + "/{email}", EMAIL))
                .andExpect(status().isOk());
        // Then
        then(authUserService).should().resetPassword(EMAIL);
    }

    @Test
    void deleteUser() throws Exception {
        // When
        mockMvc.perform(delete(USER_URL + "/{username}", USERNAME))
                .andExpect(status().isOk());
        // Then
        then(authUserService).should().deleteUserByUsername(USERNAME);
    }

    @Test
    void getTemporaryProfileImage() throws Exception {
        // When
        mockMvc.perform(get(USER_URL + IMAGE_PROFILE_URL + "/{username}", USERNAME))
                .andExpect(status().isOk());
        // Then
        then(temporaryProfileImagesService).should().getImage(USERNAME);
    }
}
