package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.services.AnswerService;
import io.dpopkov.knowthenix.services.dto.AnswerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.dpopkov.knowthenix.config.AppConstants.ANSWERS_URL;
import static io.dpopkov.knowthenix.config.AppConstants.QUESTIONS_ANSWERS_URL;

@RestController
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping(QUESTIONS_ANSWERS_URL)
    public ResponseEntity<List<AnswerDto>> getAllAnswers(@PathVariable("questionId") Long questionId) {
        List<AnswerDto> all = answerService.getAllForQuestion(questionId);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping(ANSWERS_URL)
    public ResponseEntity<AnswerDto> createNewAnswer(@RequestBody AnswerDto answerDto) {
        AnswerDto created = answerService.create(answerDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping(ANSWERS_URL + "/{answerId}")
    public ResponseEntity<AnswerDto> getAnswerById(@PathVariable("answerId") Long answerId) {
        AnswerDto found = answerService.getById(answerId);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PutMapping(ANSWERS_URL)
    public ResponseEntity<AnswerDto> updateAnswer(@RequestBody AnswerDto answerDto) {
        AnswerDto updated = answerService.update(answerDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
