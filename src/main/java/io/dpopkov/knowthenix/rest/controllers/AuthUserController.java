package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.rest.AppHttpResponse;
import io.dpopkov.knowthenix.rest.model.request.AddUpdateUserRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.dpopkov.knowthenix.domain.entities.user.Authority.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class AuthUserController {

    private static final String AN_EMAIL_SENT_TO = "An email with a new password sent to (not really): ";
    private static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";

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
        AuthUserEntity loginUser = authUserService.findByUsername(user.getUsername());
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

    @PostMapping
//    @PreAuthorize("hasAuthority('" + USER_CREATE + "')")
    public ResponseEntity<AuthUserEntity> addNewUser(@RequestBody AddUpdateUserRequest request) {
        AuthUserEntity newUser = authUserService.addNewUser(request.getFirstName(), request.getLastName(),
                request.getUsername(), request.getEmail(), request.getRole(),
                request.getNotLocked(), request.getActive());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping
//    @PreAuthorize("hasAuthority('" + USER_UPDATE + "')")
    public ResponseEntity<AuthUserEntity> updateUser(@RequestBody AddUpdateUserRequest request) {
        AuthUserEntity updated = authUserService.updateUser(request.getCurrentUsername(), request.getFirstName(),
                request.getLastName(), request.getUsername(), request.getEmail(), request.getRole(),
                request.getNotLocked(), request.getActive());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{username}")
//    @PreAuthorize("hasAuthority('" + USER_READ + "')")
    public ResponseEntity<AuthUserEntity> getByUsername(@PathVariable("username") String username) {
        AuthUserEntity user = authUserService.findByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('" + USER_READ + "')")
    public ResponseEntity<List<AuthUserEntity>> getAllUsers() {
        List<AuthUserEntity> all = authUserService.getAllUsers();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/resetPassword/{email}")
//    @PreAuthorize("hasAuthority('" + USER_UPDATE + "')")
    public ResponseEntity<AppHttpResponse> resetPassword(@PathVariable("email") String email) {
        authUserService.resetPassword(email);
        return new ResponseEntity<>(new AppHttpResponse(HttpStatus.OK, AN_EMAIL_SENT_TO + email), HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
//    @PreAuthorize("hasAuthority('" + USER_DELETE + "')")
    public ResponseEntity<AppHttpResponse> deleteUser(@PathVariable("username") String username) {
        authUserService.deleteUserByUsername(username);
        return new ResponseEntity<>(new AppHttpResponse(HttpStatus.OK, USER_DELETED_SUCCESSFULLY), HttpStatus.OK);
    }

    // todo: add methods to update and get user profile image
}
