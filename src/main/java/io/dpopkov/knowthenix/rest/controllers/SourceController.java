package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.rest.exceptions.AppControllerException;
import io.dpopkov.knowthenix.rest.model.response.ErrorMessages;
import io.dpopkov.knowthenix.services.SourceService;
import io.dpopkov.knowthenix.services.dto.SourceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.dpopkov.knowthenix.config.AppConstants.SOURCES_URL;
import static io.dpopkov.knowthenix.shared.Utils.anyFieldIsMissing;
import static io.dpopkov.knowthenix.shared.Utils.idIsMissing;

@Slf4j
@RestController
@RequestMapping(SOURCES_URL)
public class SourceController {

    private final SourceService sourceService;

    public SourceController(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @PostMapping
    public ResponseEntity<SourceDto> createSource(@RequestBody SourceDto source) {
        if (anyFieldIsMissing(source.getName(), source.getSourceType())) {
            throw new AppControllerException(ErrorMessages.MISSING_REQUIRED_FIELD);
        }
        SourceDto created = sourceService.create(source);
        log.debug("Created Source {}", created);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{sourceId}")
    public ResponseEntity<SourceDto> getSourceById(@PathVariable("sourceId") Long sourceId) {
        return new ResponseEntity<>(sourceService.getById(sourceId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SourceDto>> getAllSources() {
        return new ResponseEntity<>(sourceService.getAll(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SourceDto> updateSource(@RequestBody SourceDto source) {
        if (idIsMissing(source)) {
            throw new AppControllerException(ErrorMessages.MISSING_ID);
        }
        return new ResponseEntity<>(sourceService.update(source), HttpStatus.OK);
    }

    @DeleteMapping("/{sourceId}")
    public ResponseEntity<?> deleteSourceById(@PathVariable("sourceId") Long sourceId) {
        sourceService.delete(sourceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
