package io.dpopkov.knowthenix.rest.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.dpopkov.knowthenix.services.CategoryService;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static io.dpopkov.knowthenix.config.AppConstants.CATEGORIES_URL;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private static final Long ID = 12L;
    private static final String NAME = "name1";
    private static final String DESCRIPTION = "desc1";
    private static final String NAME_UPD = "name12";
    private static final String DESCRIPTION_UPD = "desc12";

    private final JsonMapper mapper = new JsonMapper();
    @Mock
    CategoryService service;
    @InjectMocks
    CategoryController controller;
    @Captor
    ArgumentCaptor<CategoryDto> captor;
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createCategory() throws Exception {
        // Given
        String postJson = mapper.writeValueAsString(new CategoryDto(NAME, DESCRIPTION));
        CategoryDto returnDto = new CategoryDto(NAME, DESCRIPTION);
        returnDto.setId(ID);
        given(service.create(any(CategoryDto.class))).willReturn(returnDto);
        // When
        mockMvc.perform(post(CATEGORIES_URL)
                        .contentType(APPLICATION_JSON)
                        .content(postJson)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(ID.intValue())))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.description", is(DESCRIPTION)));
        // Then
        then(service).should().create(captor.capture());
        CategoryDto toCreate = captor.getValue();
        assertEquals(NAME, toCreate.getName());
        assertEquals(DESCRIPTION, toCreate.getDescription());
    }

    @Test
    void getAllCategories() throws Exception {
        // When
        mockMvc.perform(get(CATEGORIES_URL))
                .andExpect(status().isOk());
        // Then
        then(service).should().getAll();
    }

    @Test
    void getCategoryById() throws Exception {
        // When
        mockMvc.perform(get(CATEGORIES_URL + "/" + ID))
                .andExpect(status().isOk());
        // Then
        then(service).should().getById(ID);
    }

    @Test
    void updateCategory() throws Exception {
        // Given
        CategoryDto putDto = new CategoryDto(NAME_UPD, DESCRIPTION_UPD);
        putDto.setId(ID);
        String putJson = mapper.writeValueAsString(putDto);
        CategoryDto returnDto = new CategoryDto(NAME_UPD, DESCRIPTION_UPD);
        returnDto.setId(ID);
        given(service.update(any(CategoryDto.class))).willReturn(returnDto);
        // When
        mockMvc.perform(put(CATEGORIES_URL)
                .contentType(APPLICATION_JSON)
                .content(putJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(ID.intValue())))
                .andExpect(jsonPath("$.name", is(NAME_UPD)))
                .andExpect(jsonPath("$.description", is(DESCRIPTION_UPD)));
        // Then
        then(service).should().update(captor.capture());
        CategoryDto toUpdate = captor.getValue();
        assertEquals(ID, toUpdate.getId());
        assertEquals(NAME_UPD, toUpdate.getName());
        assertEquals(DESCRIPTION_UPD, toUpdate.getDescription());
    }

    @Test
    void deleteCategory() throws Exception {
        // When
        mockMvc.perform(delete(CATEGORIES_URL + "/" + ID))
                .andExpect(status().isNoContent());
        // Then
        then(service).should().delete(ID);
    }
}
