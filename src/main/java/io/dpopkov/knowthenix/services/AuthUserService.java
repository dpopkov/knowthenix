package io.dpopkov.knowthenix.services;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.domain.entities.user.Role;
import io.dpopkov.knowthenix.services.dto.AuthUserDto;
import io.dpopkov.knowthenix.services.exceptions.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AuthUserService {

    /** Used in case when a user is not logged in and needs to register itself. */
    AuthUserDto register(String firstName, String lastName, String username, String email, String password)
            throws UsernameExistsException, EmailExistsException;

    AuthUserDto registerWithRole(String firstName, String lastName, String username, String email, Role role,
                                 String password)
            throws UsernameExistsException, EmailExistsException;

    List<AuthUserDto> getAllUsers();

    AuthUserDto findByUsername(String username) throws UserNotFoundException;

    AuthUserEntity findEntityByUsername(String username) throws UserNotFoundException;

    AuthUserDto convert(AuthUserEntity entity);

    AuthUserDto findByEmail(String email) throws UserNotFoundException;

    /** Used in case when a user is logged in and needs to add another user. */
    AuthUserDto addNewUser(String firstName, String lastName, String username, String email,
                        String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage)
            throws EmailExistsException, UsernameExistsException, IOException;

    AuthUserDto updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
                        String newEmail, String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException;

    /** Archives the user without deleting it permanently. */
    void deleteUserByUsername(String username);

    /** Deletes the user permanently. */
    void deleteUserPermanentlyByUsername(String username) throws IOException;

    void resetPassword(String email);

    AuthUserDto updateProfileImage(String username, MultipartFile profileImage)
            throws IOException, NotAnImageFileException;
}
