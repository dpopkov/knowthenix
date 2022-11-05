package io.dpopkov.knowthenix.security.handlers;

import io.dpopkov.knowthenix.rest.AppHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.dpopkov.knowthenix.security.SecurityMessages.FORBIDDEN_MESSAGE;
import static io.dpopkov.knowthenix.utils.HttpResponseUtils.writeToHttpResponse;

/**
 * Custom entry point to take control over the Access Forbidden situation.
 */
@Component
public class JwtAuthForbidden extends Http403ForbiddenEntryPoint {

    @Override
    public void commence(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AuthenticationException ex)
            throws IOException {
        AppHttpResponse appResponse = new AppHttpResponse(HttpStatus.FORBIDDEN, FORBIDDEN_MESSAGE);
        writeToHttpResponse(httpResponse, appResponse);
    }
}
