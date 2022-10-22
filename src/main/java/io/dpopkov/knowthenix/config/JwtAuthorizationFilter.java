package io.dpopkov.knowthenix.config;

import io.dpopkov.knowthenix.services.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        log.trace("JwtAuthorizationFilter created");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.trace("Request URL: {}", request.getRequestURL());
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            log.trace("No Bearer authorization found");
            chain.doFilter(request, response);
            return;
        }
        if (jwtService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            assert wac != null;
            jwtService = wac.getBean(JwtService.class);
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(header);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String header) {
        String jwtToken = header.substring(BEARER_PREFIX.length());
        try {
            String payload = jwtService.validateToken(jwtToken);
            JsonParser parser = JsonParserFactory.getJsonParser();
            Map<String, Object> payloadMap = parser.parseMap(payload);
            String username = payloadMap.get("user").toString();
            String role = payloadMap.get("role").toString();
            log.trace("Authenticated user '{}' with role '{}'", username, role);
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (Exception e) {
            return null;
        }
    }
}
