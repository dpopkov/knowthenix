package io.dpopkov.knowthenix.rest.exceptions;

import io.dpopkov.knowthenix.rest.model.response.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppControllerException extends RuntimeException {

    public AppControllerException(ErrorMessages message) {
        super(message.getErrorMessage());
    }
}
