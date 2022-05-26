package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.services.QuestionService;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.dpopkov.knowthenix.config.AppConstants.QUESTIONS_URL;

@RestController
@RequestMapping(QUESTIONS_URL)
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionDto>> getAllQuestions() {   // todo: add paging
        return new ResponseEntity<>(questionService.getAll(), HttpStatus.OK);
    }
}
