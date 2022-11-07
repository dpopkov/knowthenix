package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.rest.model.request.LoginUserRequest;
import io.dpopkov.knowthenix.rest.model.request.RegisterUserRequest;
import io.dpopkov.knowthenix.security.AuthUserPrincipal;
import io.dpopkov.knowthenix.security.JwtProvider;
import io.dpopkov.knowthenix.security.SecurityConstants;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class AuthUserController {

    private final AuthUserService authUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthUserController(AuthUserService authUserService,
                              AuthenticationManager authenticationManager,
                              JwtProvider jwtProvider) {
        this.authUserService = authUserService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthUserEntity> register(@RequestBody RegisterUserRequest user) throws AppServiceException {
        AuthUserEntity registered = authUserService.register(user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getEmail());
        // todo: fix this line below - the actual entity should not be sent as response!
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthUserEntity> login(@RequestBody LoginUserRequest user) {
        authenticate(user.getUsername(), user.getPassword());
        log.trace("User {} authenticated successfully", user.getUsername());
        AuthUserEntity loginUser = authUserService.findByUsername(user.getUsername()).orElseThrow();
        AuthUserPrincipal principal = new AuthUserPrincipal(
                loginUser.getUsername(), loginUser.getEncryptedPassword(), loginUser.getAuthorities(),
                loginUser.isNotLocked(), loginUser.isActive());
        HttpHeaders jwtHeader = createJwtHeader(principal);
        // todo: fix this line below - the actual entity should not be sent as response!!!
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    private void authenticate(String username, String password)
            throws DisabledException, LockedException, BadCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders createJwtHeader(AuthUserPrincipal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.JWT_HEADER, jwtProvider.generateToken(principal));
        return headers;
    }
}
