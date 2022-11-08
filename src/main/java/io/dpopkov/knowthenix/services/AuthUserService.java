package io.dpopkov.knowthenix.services;

import io.dpopkov.knowthenix.domain.entities.user.AuthUserEntity;
import io.dpopkov.knowthenix.services.exceptions.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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
                        String role, boolean isNotLocked, boolean isActive)
            throws EmailExistsException, UsernameExistsException;

    AuthUserEntity updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
                        String newEmail, String role, boolean isNotLocked, boolean isActive)
            throws UserNotFoundException, UsernameExistsException, EmailExistsException;

    void deleteUserByUsername(String username);

    default void resetPassword(String email) {
        // todo: implement resetting password after Notification by Email is finished
        throw new UnsupportedOperationException("Reset password is not implemented yet");
    }

    default AuthUserEntity updateProfileImage(String username, MultipartFile profileImage) {
        // todo: implement updating image after profile image is finished
        throw new UnsupportedOperationException("Updating image is not implemented yet");
    }
}
