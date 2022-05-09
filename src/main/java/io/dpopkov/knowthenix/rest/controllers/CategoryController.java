package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.rest.exceptions.AppControllerException;
import io.dpopkov.knowthenix.rest.model.response.ErrorMessages;
import io.dpopkov.knowthenix.services.CategoryService;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.dpopkov.knowthenix.shared.Utils.anyFieldIsMissing;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto category) {
        if (anyFieldIsMissing(category.getName(), category.getDescription())) {
            throw new AppControllerException(ErrorMessages.MISSING_REQUIRED_FIELD);
        }
        CategoryDto created = categoryService.create(category);
        log.debug("Created category {}", created);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
