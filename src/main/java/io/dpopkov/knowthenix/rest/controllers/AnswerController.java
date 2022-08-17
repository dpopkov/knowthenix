package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.rest.exceptions.AppControllerException;
import io.dpopkov.knowthenix.rest.model.response.ErrorMessages;
import io.dpopkov.knowthenix.services.AnswerService;
import io.dpopkov.knowthenix.services.dto.AnswerDto;
import io.dpopkov.knowthenix.services.dto.KeyTermDto;
import io.dpopkov.knowthenix.services.dto.TranslationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static io.dpopkov.knowthenix.config.AppConstants.ANSWERS_URL;
import static io.dpopkov.knowthenix.config.AppConstants.QUESTIONS_ANSWERS_URL;
import static io.dpopkov.knowthenix.shared.Utils.idIsMissing;

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

    @GetMapping(ANSWERS_URL + "/{answerId}/translations")
    public ResponseEntity<List<TranslationDto>> getTranslationsByAnswerId(@PathVariable("answerId") Long answerId) {
        List<TranslationDto> translations = answerService.getTranslations(answerId);
        return new ResponseEntity<>(translations, HttpStatus.OK);
    }

    @PostMapping(ANSWERS_URL + "/{answerId}/translations")
    public ResponseEntity<TranslationDto> addTranslation(@PathVariable("answerId") Long answerId,
                                                         @RequestBody TranslationDto translation) {
        TranslationDto created = answerService.addTranslation(answerId, translation);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping(ANSWERS_URL + "/{answerId}/translations")
    public ResponseEntity<TranslationDto> updateTranslation(@PathVariable("answerId") Long answerId,
                                                            @RequestBody TranslationDto translation) {
        if (idIsMissing(translation)) {
            throw new AppControllerException(ErrorMessages.MISSING_ID);
        }
        TranslationDto updated = answerService.updateTranslation(answerId, translation);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping(ANSWERS_URL + "/{answerId}/keyterms")
    public ResponseEntity<Collection<KeyTermDto>> getKeyTerms(@PathVariable("answerId") Long answerId) {
        Collection<KeyTermDto> keyTerms = answerService.getKeyTermsByAnswerId(answerId);
        return new ResponseEntity<>(keyTerms, HttpStatus.OK);
    }
}
