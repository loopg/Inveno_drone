package com.inveno.drone.dto;

import java.time.Instant;

/**
 * Data Transfer Object for RPA task execution responses.
 */
public class RpaTaskResponse {

    private String status;
    private String message;
    private Instant timestamp;

    // Default constructor for JSON serialization
    public RpaTaskResponse() {}

    public RpaTaskResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public RpaTaskResponse(String status, String message, Instant timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "RpaTaskResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
