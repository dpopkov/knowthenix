package io.dpopkov.knowthenix.services.exceptions;

import io.dpopkov.knowthenix.services.AppServiceException;

public class EmailExistsException extends AppServiceException {
    public EmailExistsException(String message) {
        super(message);
    }
}
