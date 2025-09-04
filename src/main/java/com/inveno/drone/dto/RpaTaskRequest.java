package com.inveno.drone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for incoming RPA task execution requests.
 */
public class RpaTaskRequest {

    @NotBlank(message = "macroName cannot be empty")
    @Size(max = 100, message = "macroName cannot exceed 100 characters")
    private String macroName;

    // Default constructor for JSON deserialization
    public RpaTaskRequest() {}

    public RpaTaskRequest(String macroName) {
        this.macroName = macroName;
    }

    // Getters and Setters
    public String getMacroName() {
        return macroName;
    }

    public void setMacroName(String macroName) {
        this.macroName = macroName;
    }

    @Override
    public String toString() {
        return "RpaTaskRequest{" +
                "macroName='" + macroName + '\'' +
                '}';
    }
}
