package io.dpopkov.knowthenix.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static io.dpopkov.knowthenix.security.SecurityConstants.JWT_HEADER;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final AppClientProps appClientProps;

    public CorsConfig(AppClientProps appClientProps) {
        this.appClientProps = appClientProps;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Access-Control-Allow-Origin", "Content-Type", "Accept", JWT_HEADER,
                        "Authorization", "Origin, Accept", "X-Requested-With", "Access-Control-Request-Method",
                        "Access-Control-Request-Headers")
                .exposedHeaders("Origin", "Content-Type", "Accept", JWT_HEADER,
                        "Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials")
                .allowedOrigins(appClientProps.getAllowedOrigin());
    }
}
