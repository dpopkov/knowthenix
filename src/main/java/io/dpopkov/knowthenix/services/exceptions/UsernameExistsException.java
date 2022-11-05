package io.dpopkov.knowthenix.services.exceptions;

import io.dpopkov.knowthenix.services.AppServiceException;

public class UsernameExistsException extends AppServiceException {
    public UsernameExistsException(String message) {
        super(message);
    }
}
