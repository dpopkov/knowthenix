package io.dpopkov.knowthenix.rest.controllers;

import io.dpopkov.knowthenix.services.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static io.dpopkov.knowthenix.config.AppConstants.BASIC_AUTH_URL;

@Slf4j
@RestController
@RequestMapping(BASIC_AUTH_URL)
public class ValidateUserController {

    private final JwtService jwtService;

    public ValidateUserController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @RequestMapping("/validate")
    public Map<String, String> userIsValid() {
        /*
            The user has been validated at this point using Basic Authentication.
            Spring has already determined which user this is.
         */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        /*
            Generating and sending token.
         */
        String username = currentUser.getUsername();
        String role = currentUser.getAuthorities().toArray()[0].toString().substring("ROLE_".length());
        log.debug("Generating token for name '{}' and role '{}'", username, role);
        String token = jwtService.generateToken(username, role);
        log.debug("Generated token: {}", token);
        return Map.of("result", token);
    }
}
