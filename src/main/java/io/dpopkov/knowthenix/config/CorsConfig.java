package io.dpopkov.knowthenix.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static io.dpopkov.knowthenix.config.AppConstants.API;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(API + "/**")
                .allowedMethods("GET", "POST", "PUT")
                .allowedOrigins("http://localhost:4200");
    }
}
