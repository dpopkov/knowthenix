package io.dpopkov.knowthenix.rest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.dpopkov.knowthenix.config.AppConstants.BASIC_AUTH_URL;

@RestController
@RequestMapping(BASIC_AUTH_URL)
public class ValidateUserController {

    @RequestMapping("/validate")
    public String userIsValid() {
        return "{\"result\":\"ok\"}";
    }
}
