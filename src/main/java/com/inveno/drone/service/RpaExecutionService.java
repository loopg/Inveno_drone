package com.inveno.drone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Service responsible for executing ui.vision macros using ProcessBuilder.
 * This service launches the browser with the specified macro and manages the execution lifecycle.
 */
@Service
public class RpaExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(RpaExecutionService.class);

    @Value("${rpa.browser.path}")
    private String browserPath;

    @Value("${rpa.uivision.url}")
    private String uiVisionUrl;

    @Value("${rpa.execution.timeout.minutes:10}")
    private long executionTimeoutMinutes;

    /**
     * Executes a ui.vision macro by launching the browser with the specified macro.
     *
     * @param macroName the name of the macro to execute
     * @return true if execution was successful, false otherwise
     */
    public boolean executeMacro(String macroName) {
        logger.info("Starting execution of macro: {}", macroName);
        
        ProcessBuilder processBuilder = buildProcess(macroName);
        Process process = null;
        
        try {
            // Start the process
            process = processBuilder.start();
            logger.info("Browser process started for macro: {}", macroName);
            
            // Start output consumption in separate threads
            startOutputConsumption(process, macroName);
            
            // Wait for completion with timeout
            boolean completed = process.waitFor(executionTimeoutMinutes, TimeUnit.MINUTES);
            
            if (!completed) {
                logger.error("Macro execution timed out for '{}' after {} minutes", macroName, executionTimeoutMinutes);
                process.destroyForcibly();
                return false;
            }
            
            // Check exit code
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                logger.info("Macro '{}' executed successfully with exit code: {}", macroName, exitCode);
                return true;
            } else {
                logger.warn("Macro '{}' finished with non-zero exit code: {}", macroName, exitCode);
                return false;
            }
            
        } catch (IOException e) {
            logger.error("Failed to start browser process for macro '{}'", macroName, e);
            return false;
        } catch (InterruptedException e) {
            logger.error("Process execution was interrupted for macro '{}'", macroName, e);
            Thread.currentThread().interrupt();
            if (process != null) {
                process.destroyForcibly();
            }
            return false;
        } finally {
            if (process != null && process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    /**
     * Builds the ProcessBuilder with the command to launch the browser with the specified macro.
     */
    private ProcessBuilder buildProcess(String macroName) {
        // Construct the URL with macro parameter and direct=1 for automated execution
        String fullUrl = uiVisionUrl;
        if (uiVisionUrl.contains("?")) {
            fullUrl += "&macro=" + macroName + "&direct=1";
        } else {
            fullUrl += "?macro=" + macroName + "&direct=1";
        }
        
        logger.debug("Building process with browser: {} and URL: {}", browserPath, fullUrl);
        
        ProcessBuilder processBuilder = new ProcessBuilder();
        
        // For Windows, we need to handle the command differently
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            processBuilder.command(browserPath, fullUrl);
        } else {
            processBuilder.command(browserPath, fullUrl);
        }
        
        // Redirect error stream to output stream
        processBuilder.redirectErrorStream(true);
        
        return processBuilder;
    }

    /**
     * Starts separate threads to consume the process output and error streams.
     */
    private void startOutputConsumption(Process process, String macroName) {
        // Consume output stream
        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.debug("Macro '{}' output: {}", macroName, line);
                }
            } catch (IOException e) {
                logger.warn("Error reading process output for macro '{}'", macroName, e);
            }
        });
        outputThread.setDaemon(true);
        outputThread.start();
    }

    // Getters for configuration values (useful for testing)
    public String getBrowserPath() {
        return browserPath;
    }

    public String getUiVisionUrl() {
        return uiVisionUrl;
    }

    public long getExecutionTimeoutMinutes() {
        return executionTimeoutMinutes;
    }
}
