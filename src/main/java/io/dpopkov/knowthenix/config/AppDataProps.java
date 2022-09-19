package io.dpopkov.knowthenix.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.data")
public class AppDataProps {

    /** Flag whether to initialize database when application starts. */
    private boolean init = false;
}
