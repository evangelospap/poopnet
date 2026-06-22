package com.poopvibe.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts the Poop Vibe Spring Boot API.
 */
@SpringBootApplication
public class PoopVibeApplication {

    /**
     * Launches the application with the configured Spring profile and embedded server.
     *
     * @param args command-line arguments passed through to Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(PoopVibeApplication.class, args);
    }
}
