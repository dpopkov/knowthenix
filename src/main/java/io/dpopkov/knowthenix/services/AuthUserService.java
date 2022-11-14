package io.dpopkov.knowthenix.services;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.services.exceptions.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AuthUserService {

    /** Used in case when a user is not logged in and needs to register itself. */
    AuthUserEntity register(String firstName, String lastName, String username, String email)
            throws UsernameExistsException, EmailExistsException;

    List<AuthUserEntity> getAllUsers();

    AuthUserEntity findByUsername(String username) throws UserNotFoundException;

    AuthUserEntity findByEmail(String email) throws UserNotFoundException;

    // todo: for next 2 methods add MultipartFile for profile image

    /** Used in case when a user is logged in and needs to add another user. */
    AuthUserEntity addNewUser(String firstName, String lastName, String username, String email,
                        String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage)
            throws EmailExistsException, UsernameExistsException, IOException;

    AuthUserEntity updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
                        String newEmail, String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistsException, EmailExistsException, IOException;

    void deleteUserByUsername(String username);

    void resetPassword(String email);

    AuthUserEntity updateProfileImage(String username, MultipartFile profileImage);
}
