package io.dpopkov.knowthenix.rest.exceptions;

import io.dpopkov.knowthenix.rest.model.response.ErrorMessages;

public class AppControllerException extends RuntimeException {

    public AppControllerException(ErrorMessages message) {
        super(message.getErrorMessage());
    }
}
