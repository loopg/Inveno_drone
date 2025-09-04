package com.inveno.drone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for the Inveno Drone Server.
 * This service provides API endpoints to execute ui.vision macros.
 */
@SpringBootApplication
public class DroneServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroneServerApplication.class, args);
    }
}
