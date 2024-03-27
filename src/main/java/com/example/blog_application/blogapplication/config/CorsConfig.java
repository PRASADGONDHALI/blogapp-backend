package com.example.blog_application.blogapplication.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Specify the allowed origins (e.g., "http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify the allowed HTTP methods
                        .allowedHeaders("*"); // Specify the allowed headers
            }
        };
    }
}