package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.rest.exceptions.AppControllerException;
import io.dpopkov.knowthenix.rest.model.response.ErrorMessages;
import io.dpopkov.knowthenix.services.QuestionService;
import io.dpopkov.knowthenix.services.dto.KeyTermDto;
import io.dpopkov.knowthenix.services.dto.QuestionDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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

    @PostMapping
    public ResponseEntity<QuestionDto> addNewQuestion(@RequestBody QuestionDto questionDto) {
        QuestionDto saved = questionService.create(questionDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<QuestionDto> updateQuestion(@RequestBody QuestionDto questionDto) {
        if (idIsMissing(questionDto)) {
            throw new AppControllerException(ErrorMessages.MISSING_ID);
        }
        QuestionDto updated = questionService.update(questionDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{questionId}/translations")
    public ResponseEntity<List<TranslationDto>> getTranslationsByQuestionId(@PathVariable("questionId") Long questionId) {
        List<TranslationDto> translations = questionService.getTranslations(questionId);
        return new ResponseEntity<>(translations, HttpStatus.OK);
    }

    @PostMapping("/{questionId}/translations")
    public ResponseEntity<TranslationDto> addTranslation(@PathVariable("questionId") Long questionId,
                                                         @RequestBody TranslationDto translation) {
        TranslationDto created = questionService.addTranslation(questionId, translation);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{questionId}/translations")
    public ResponseEntity<TranslationDto> updateTranslation(@PathVariable("questionId") Long questionId,
                                                            @RequestBody TranslationDto translation) {
        if (idIsMissing(translation)) {
            throw new AppControllerException(ErrorMessages.MISSING_ID);
        }
        TranslationDto updated = questionService.updateTranslation(questionId, translation);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{questionId}/keyterms")
    public ResponseEntity<Collection<KeyTermDto>> getKeyTerms(@PathVariable("questionId") Long questionId) {
        Collection<KeyTermDto> keyTerms = questionService.getKeyTermsByQuestionId(questionId);
        return new ResponseEntity<>(keyTerms, HttpStatus.OK);
    }
}
