package com.interviewme.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for web-related settings in the InterviewMe application.
 * Implements WebMvcConfigurer to customize Spring MVC configuration.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings for the application.
     * This method allows specific origins, methods, and headers for API endpoints.
     *
     * @param registry the CorsRegistry to configure CORS settings
     *
     * Configuration details:
     * - Applies to all endpoints under /api/**
     * - Allows requests from http://localhost:3000 (frontend development server)
     * - Permits GET, POST, PUT, DELETE, and OPTIONS HTTP methods
     * - Allows all headers in requests
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
