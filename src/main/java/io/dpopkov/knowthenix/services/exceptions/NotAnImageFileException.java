package io.dpopkov.knowthenix.services.exceptions;

import io.dpopkov.knowthenix.services.AppServiceException;

public class NotAnImageFileException extends AppServiceException {
    public NotAnImageFileException(String message) {
        super(message);
    }
}
