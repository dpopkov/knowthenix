package io.dpopkov.knowthenix.services.exceptions;

import io.dpopkov.knowthenix.services.AppServiceException;

public class UserNotFoundException extends AppServiceException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
