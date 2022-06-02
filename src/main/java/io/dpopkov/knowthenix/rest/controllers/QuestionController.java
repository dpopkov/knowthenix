package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.rest.exceptions.AppControllerException;
import io.dpopkov.knowthenix.rest.model.response.ErrorMessages;
import io.dpopkov.knowthenix.services.QuestionService;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.dpopkov.knowthenix.config.AppConstants.QUESTIONS_URL;
import static io.dpopkov.knowthenix.shared.Utils.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> getQuestion(@PathVariable("id") Long id) {
        QuestionDto dto = questionService.getById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<QuestionDto> updateQuestion(@RequestBody QuestionDto questionDto) {
        if (idIsMissing(questionDto)) {
            throw new AppControllerException(ErrorMessages.MISSING_ID);
        }
        QuestionDto updated = questionService.update(questionDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PostMapping("/{questionId}/translations")
    public ResponseEntity<TranslationDto> addTranslation(@PathVariable("questionId") Long questionId,
                                                         @RequestBody TranslationDto translation) {
        TranslationDto created = questionService.addTranslation(questionId, translation);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
