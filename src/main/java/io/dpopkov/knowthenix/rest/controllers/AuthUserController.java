package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.config.FileConstants;
import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.rest.AppHttpResponse;
import io.dpopkov.knowthenix.rest.model.request.LoginUserRequest;
import io.dpopkov.knowthenix.rest.model.request.RegisterUserRequest;
import io.dpopkov.knowthenix.security.AuthUserPrincipal;
import io.dpopkov.knowthenix.security.JwtProvider;
import io.dpopkov.knowthenix.security.SecurityConstants;
import io.dpopkov.knowthenix.services.AppServiceException;
import io.dpopkov.knowthenix.services.AuthUserService;
import io.dpopkov.knowthenix.services.TemporaryProfileImagesService;
import io.dpopkov.knowthenix.services.dto.AuthUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.dpopkov.knowthenix.config.AppConstants.*;
import static io.dpopkov.knowthenix.domain.entities.user.Authority.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@Slf4j
@RestController
@RequestMapping(USER_URL)
public class AuthUserController {

    private static final String AN_EMAIL_SENT_TO = "An email with a new password sent to (not really): ";
    private static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";

    private final AuthUserService authUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final TemporaryProfileImagesService temporaryProfileImagesService;

    public AuthUserController(AuthUserService authUserService,
                              AuthenticationManager authenticationManager,
                              JwtProvider jwtProvider,
                              TemporaryProfileImagesService temporaryProfileImagesService) {
        this.authUserService = authUserService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.temporaryProfileImagesService = temporaryProfileImagesService;
    }

    @PostMapping(REGISTER_URL)
    public ResponseEntity<AuthUserDto> register(@RequestBody RegisterUserRequest user) throws AppServiceException {
        AuthUserDto registered = authUserService.register(user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getEmail(), user.getPassword());
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

    @PostMapping(LOGIN_URL)
    public ResponseEntity<AuthUserDto> login(@RequestBody LoginUserRequest user) {
        authenticate(user.getUsername(), user.getPassword());
        log.trace("User {} authenticated successfully", user.getUsername());
        AuthUserEntity loginUser = authUserService.findEntityByUsername(user.getUsername());
        AuthUserPrincipal principal = new AuthUserPrincipal(
                loginUser.getUsername(), loginUser.getEncryptedPassword(), loginUser.getAuthorities(),
                loginUser.isNotLocked(), loginUser.isActive());
        HttpHeaders jwtHeader = createJwtHeader(principal);
        AuthUserDto dto = authUserService.convert(loginUser);
        return new ResponseEntity<>(dto, jwtHeader, HttpStatus.OK);
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
    @PreAuthorize("hasAuthority('" + USER_CREATE + "')")
    public ResponseEntity<AuthUserDto> addNewUser(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("role") String role,
            @RequestParam("notLocked") String notLocked,
            @RequestParam("active") String active,
            @RequestParam(value = "profileImage", required = false) MultipartFile image) throws IOException {
        AuthUserDto newUser = authUserService.addNewUser(firstName, lastName, username, email, role,
                Boolean.parseBoolean(notLocked), Boolean.parseBoolean(active), image);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('" + USER_UPDATE + "')")
    public ResponseEntity<AuthUserDto> updateUser(
            @RequestParam("currentUsername") String currentUsername,
            @RequestParam("firstName") String newFirstName,
            @RequestParam("lastName") String newLastName,
            @RequestParam("username") String newUsername,
            @RequestParam("email") String newEmail,
            @RequestParam("role") String newRole,
            @RequestParam("notLocked") String newNotLocked,
            @RequestParam("active") String newActive,
            @RequestParam(value = "profileImage", required = false) MultipartFile image) throws IOException {
        AuthUserDto updated = authUserService.updateUser(
                currentUsername, newFirstName, newLastName, newUsername, newEmail, newRole,
                Boolean.parseBoolean(newNotLocked), Boolean.parseBoolean(newActive), image);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('" + USER_READ + "')")
    public ResponseEntity<AuthUserDto> getByUsername(@PathVariable("username") String username) {
        AuthUserDto user = authUserService.findByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('" + USER_READ + "')")
    public ResponseEntity<List<AuthUserDto>> getAllUsers() {
        List<AuthUserDto> all = authUserService.getAllUsers();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PutMapping(RESET_PASSWORD_URL + "/{email}")
    @PreAuthorize("hasAuthority('" + USER_UPDATE + "')")
    public ResponseEntity<AppHttpResponse> resetPassword(@PathVariable("email") String email) {
        authUserService.resetPassword(email);
        return new ResponseEntity<>(new AppHttpResponse(HttpStatus.OK, AN_EMAIL_SENT_TO + email), HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('" + USER_DELETE + "')")
    public ResponseEntity<AppHttpResponse> deleteUser(@PathVariable("username") String username) throws IOException {
        authUserService.deleteUserByUsername(username);
        return new ResponseEntity<>(new AppHttpResponse(HttpStatus.OK, USER_DELETED_SUCCESSFULLY), HttpStatus.OK);
    }

    @GetMapping(path = IMAGE_PROFILE_URL + "/{username}", produces = IMAGE_PNG_VALUE)
    public byte[] getTemporaryProfileImage(@PathVariable String username) {
        return temporaryProfileImagesService.getImage(username);
    }

    @GetMapping(path = IMAGE_URL + "/{username}/{filename}", produces = IMAGE_PNG_VALUE)
    public byte[] getProfileImage(@PathVariable String username, @PathVariable String filename) throws IOException {
        // todo: move all operations with images to image service
        return Files.readAllBytes(Paths.get(FileConstants.USER_FOLDER, username, filename));
    }

    @PutMapping(UPDATE_PROFILE_IMAGE_URL)
    public ResponseEntity<AuthUserDto> updateProfileImage(@RequestParam String username,
                                                             @RequestParam(value = "profileImage") MultipartFile image)
            throws IOException {
        AuthUserDto updatedUser = authUserService.updateProfileImage(username, image);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
