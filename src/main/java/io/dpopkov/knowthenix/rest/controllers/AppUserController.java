package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.rest.exceptions.AppControllerException;
import io.dpopkov.knowthenix.rest.model.response.ErrorMessages;
import io.dpopkov.knowthenix.services.AppUserService;
import io.dpopkov.knowthenix.services.dto.AppUserCreateDto;
import io.dpopkov.knowthenix.services.dto.AppUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.dpopkov.knowthenix.config.AppConstants.USERS_URL;
import static io.dpopkov.knowthenix.shared.Utils.anyFieldIsMissing;
import static io.dpopkov.knowthenix.shared.Utils.idIsMissing;

@Slf4j
@RestController
@RequestMapping(USERS_URL)
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<AppUserDto> createUser(@RequestBody AppUserCreateDto user) {
        if (anyFieldIsMissing(user.getName(), user.getPassword())) {
            throw new AppControllerException(ErrorMessages.MISSING_REQUIRED_FIELD);
        }
        AppUserDto created = appUserService.create(user);
        log.debug("Created User {}", created.getName());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AppUserDto>> getAllUsers() {
        return new ResponseEntity<>(appUserService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AppUserDto> getUser(@PathVariable("userId") Long id) {
        return new ResponseEntity<>(appUserService.getById(id), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<AppUserDto> updateUser(@RequestBody AppUserDto user) {
        if (idIsMissing(user)) {
            throw new AppControllerException(ErrorMessages.MISSING_ID);
        }
        AppUserDto updated = appUserService.update(user);
        log.debug("Updated User {}", updated.getName());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long id) {
        appUserService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/resetPassword/{userId}")
    public ResponseEntity<?> resetPassword(@PathVariable("userId") Long id) {
        appUserService.resetPassword(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
