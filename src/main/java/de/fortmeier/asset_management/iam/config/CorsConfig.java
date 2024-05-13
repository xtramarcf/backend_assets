package de.fortmeier.asset_management.iam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for the cross-origin resource sharing policy.
 */
@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {


    /**
     * Adds a corsMapping with the allowed origins, methods and headers.
     * @param registry injects the CorsRegistry.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://frontend:4200", "http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }
}
