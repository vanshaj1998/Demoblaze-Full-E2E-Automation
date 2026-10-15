package com.example.demoblaze.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Captures and persists failure screenshots.
 * Attaches to Allure and saves to target/screenshots for CI build archiving.
 */
public final class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");

    private ScreenshotUtils() {}

    public static void captureAndAttach(WebDriver driver, String testName) {
        if (!(driver instanceof TakesScreenshot ts)) {
            log.warn("Driver does not support TakesScreenshot – skipping capture.");
            return;
        }
        try {
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);

            // Attach to Allure
            String attachName = "Screenshot – " + testName;
            Allure.addAttachment(attachName, new ByteArrayInputStream(screenshot));

            // Write to target/screenshots/
            Files.createDirectories(SCREENSHOT_DIR);
            String filename = sanitize(testName) + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png";
            Path dest = SCREENSHOT_DIR.resolve(filename);
            Files.write(dest, screenshot);
            log.info("Screenshot saved: {}", dest.toAbsolutePath());

        } catch (IOException e) {
            log.error("Failed to save screenshot for '{}': {}", testName, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error capturing screenshot: {}", e.getMessage());
        }
    }

    private static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9_\\-]", "_").replaceAll("_+", "_");
    }
}
