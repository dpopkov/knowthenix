package io.dpopkov.knowthenix.services.exceptions;

import io.dpopkov.knowthenix.services.AppServiceException;

public class EmailNotFoundException extends AppServiceException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
