package com.inveno.drone.controller;

import com.inveno.drone.dto.RpaTaskRequest;
import com.inveno.drone.dto.RpaTaskResponse;
import com.inveno.drone.service.RpaExecutionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * REST Controller for RPA task execution endpoints.
 * Provides API endpoints to trigger and monitor ui.vision macro execution.
 */
@RestController
@RequestMapping("/api/v1/rpa")
@CrossOrigin(origins = "*") // For development - restrict in production
public class RpaController {

    private static final Logger logger = LoggerFactory.getLogger(RpaController.class);

    private final RpaExecutionService rpaExecutionService;

    @Autowired
    public RpaController(RpaExecutionService rpaExecutionService) {
        this.rpaExecutionService = rpaExecutionService;
    }

    /**
     * Executes a ui.vision macro asynchronously.
     *
     * @param request the RPA task request containing the macro name
     * @return response indicating success or failure of the execution
     */
    @PostMapping("/run")
    public ResponseEntity<RpaTaskResponse> runRpaTask(@Valid @RequestBody RpaTaskRequest request) {
        logger.info("Received RPA task execution request for macro: {}", request.getMacroName());
        
        try {
            // Execute the macro
            boolean success = rpaExecutionService.executeMacro(request.getMacroName());
            
            if (success) {
                RpaTaskResponse response = new RpaTaskResponse(
                    "SUCCESS",
                    String.format("Macro '%s' executed successfully.", request.getMacroName())
                );
                logger.info("Macro '{}' execution completed successfully", request.getMacroName());
                return ResponseEntity.ok(response);
            } else {
                RpaTaskResponse response = new RpaTaskResponse(
                    "FAILED",
                    String.format("Macro '%s' execution failed. Check server logs for details.", request.getMacroName())
                );
                logger.error("Macro '{}' execution failed", request.getMacroName());
                return ResponseEntity.status(500).body(response);
            }
            
        } catch (Exception e) {
            logger.error("Unexpected error during macro '{}' execution", request.getMacroName(), e);
            RpaTaskResponse response = new RpaTaskResponse(
                "FAILED",
                "Macro execution failed due to an unexpected error. Check server logs for details."
            );
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Health check endpoint to verify the service is running.
     *
     * @return simple health status
     */
    @GetMapping("/health")
    public ResponseEntity<RpaTaskResponse> healthCheck() {
        RpaTaskResponse response = new RpaTaskResponse(
            "HEALTHY",
            "Drone Server is running and ready to execute macros."
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get the current configuration of the Drone Server.
     * Useful for debugging and verification.
     *
     * @return current configuration details
     */
    @GetMapping("/config")
    public ResponseEntity<RpaTaskResponse> getConfiguration() {
        String configInfo = String.format(
            "Browser Path: %s, UI Vision URL: %s, Timeout: %d minutes",
            rpaExecutionService.getBrowserPath(),
            rpaExecutionService.getUiVisionUrl(),
            rpaExecutionService.getExecutionTimeoutMinutes()
        );
        
        RpaTaskResponse response = new RpaTaskResponse(
            "CONFIG",
            configInfo
        );
        return ResponseEntity.ok(response);
    }
}
