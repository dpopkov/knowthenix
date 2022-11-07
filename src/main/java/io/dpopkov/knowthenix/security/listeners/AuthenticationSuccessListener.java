package io.dpopkov.knowthenix.security.listeners;

import io.dpopkov.knowthenix.services.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationSuccessListener {

    private final LoginAttemptService loginAttemptService;

    public AuthenticationSuccessListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        final Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            log.trace("Authentication successful for user '{}'", user.getUsername());
            loginAttemptService.evictUser(user.getUsername());
        } else {
            log.warn("This principal is not instance of UserDetails: {}", principal);
        }
    }
}
