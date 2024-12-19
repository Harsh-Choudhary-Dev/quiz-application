package com.neet_question_api.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow CORS for all endpoints
                .allowedOrigins("http://127.0.0.1:5500", "http://localhost:5500","http://127.0.0.1:5501", "http://localhost:5501","http://127.0.0.1:5502", "http://localhost:5502") // Allow requests from these origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials (e.g., cookies, headers like x-api-key)
    }
}
