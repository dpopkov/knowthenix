package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.rest.exceptions.AppControllerException;
import io.dpopkov.knowthenix.rest.model.response.ErrorMessages;
import io.dpopkov.knowthenix.services.KeyTermService;
import io.dpopkov.knowthenix.services.dto.KeyTermDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.dpopkov.knowthenix.config.AppConstants.KEYTERMS_URL;
import static io.dpopkov.knowthenix.shared.Utils.anyFieldIsMissing;

@Slf4j
@RestController
@RequestMapping(KEYTERMS_URL)
public class KeyTermController {

    private final KeyTermService keyTermService;

    public KeyTermController(KeyTermService keyTermService) {
        this.keyTermService = keyTermService;
    }

    @PostMapping
    public ResponseEntity<KeyTermDto> createKeyTerm(@RequestBody KeyTermDto keyTerm) {
        if (anyFieldIsMissing(keyTerm.getName())) {
            throw new AppControllerException(ErrorMessages.MISSING_REQUIRED_FIELD);
        }
        KeyTermDto created = keyTermService.create(keyTerm);
        log.debug("Created keyTerm {}", created);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<KeyTermDto>> getAllCategories() {   // todo: add paging
        return new ResponseEntity<>(keyTermService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeyTermDto> getCategoryById(@PathVariable Long id) {
        return new ResponseEntity<>(keyTermService.getById(id), HttpStatus.OK);
    }
}
