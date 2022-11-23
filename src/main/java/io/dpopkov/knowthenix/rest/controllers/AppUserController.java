package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.services.AppUserService;
import io.dpopkov.knowthenix.services.dto.AppUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.dpopkov.knowthenix.config.AppConstants.USERS_URL;

@Slf4j
@RestController
@RequestMapping(USERS_URL)
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<List<AppUserDto>> getAllUsers() {
        return new ResponseEntity<>(appUserService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AppUserDto> getUser(@PathVariable("userId") Long id) {
        return new ResponseEntity<>(appUserService.getById(id), HttpStatus.OK);
    }
}
