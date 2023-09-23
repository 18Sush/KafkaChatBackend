package com.rest;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogbackConfigurationTest {

    @Test
    public void testLogMessagesToFile() throws Exception {
        // Define the logger using the same name as in your Logback configuration
        Logger logger = LoggerFactory.getLogger("com.proj.kafkachatserver.controllers.ChatController");

        // Log a test message
        logger.info("Test log message");

        // Wait for a moment to allow the log to be written
        Thread.sleep(1000);

        // Check if the log message is present in the log file
        assertTrue(isLogMessagePresent("C:/Users/e019851/Documents/disaster2.txt", "Test log message"));
    }

    private boolean isLogMessagePresent(String logFilePath, String expectedMessage) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(expectedMessage)) {
                    return true;
                }
            }
        }
        return false;
    }
}