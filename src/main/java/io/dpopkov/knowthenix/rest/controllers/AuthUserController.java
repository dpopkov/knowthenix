package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.AuthUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthUserController {

    private final AuthUserService authUserService;

    public AuthUserController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    // todo: make a UserRegisterRequest class
    @PostMapping("/register")
    public ResponseEntity<AuthUserEntity> register(@RequestBody AuthUserEntity user) throws AppServiceException {
        AuthUserEntity registered = authUserService.register(user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getEmail());
        // todo: fix this line below - the actual entity should not be sent as response!
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }
}
