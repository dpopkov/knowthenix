package io.dpopkov.knowthenix.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.client")
public class AppClientProps {

    private static final String DEFAULT_ALLOWED_ORIGIN = "http://localhost:4200";

    private String allowedOrigin = DEFAULT_ALLOWED_ORIGIN;
}
