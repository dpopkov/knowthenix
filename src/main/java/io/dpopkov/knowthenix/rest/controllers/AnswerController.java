package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.services.AnswerService;
import io.dpopkov.knowthenix.services.dto.AnswerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.dpopkov.knowthenix.config.AppConstants.ANSWERS_URL;

@RestController
@RequestMapping(ANSWERS_URL)
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public ResponseEntity<AnswerDto> createNewAnswer(@RequestBody AnswerDto answerDto) {
        AnswerDto created = answerService.create(answerDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
