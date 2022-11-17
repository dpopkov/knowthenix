package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.rest.exceptions.AppControllerException;
import io.dpopkov.knowthenix.rest.model.response.ErrorMessages;
import io.dpopkov.knowthenix.services.CategoryService;
import io.dpopkov.knowthenix.services.dto.CategoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.dpopkov.knowthenix.config.AppConstants.CATEGORIES_URL;
import static io.dpopkov.knowthenix.shared.Utils.*;

@Slf4j
@RestController
@RequestMapping(CATEGORIES_URL)
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto category,
                                                      @AuthenticationPrincipal Object principal) {
        if (anyFieldIsMissing(category.getName())) {
            throw new AppControllerException(ErrorMessages.MISSING_REQUIRED_FIELD);
        }
        category.setCreatedBy(principal.toString());
        CategoryDto created = categoryService.create(category);
        log.debug("Created category {}", created);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {   // todo: add paging
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return new ResponseEntity<>(categoryService.getById(id), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto category) {
        if (anyFieldOrIdIsMissing(category, category.getName())) {
            throw new AppControllerException(ErrorMessages.MISSING_REQUIRED_FIELD);
        }
        CategoryDto updated = categoryService.update(category);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
