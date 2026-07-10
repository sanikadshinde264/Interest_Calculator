package com.yourcompany.interestcalc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InterestCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterestCalculatorApplication.class, args);
    }

    /**
     * Allows the static frontend (served from a different origin/port during
     * development, e.g. a Live Server on :5500) to call this API on :8080.
     * Tighten allowedOrigins before deploying to production.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "DELETE", "OPTIONS");
            }
        };
    }
}
