package com.example.demoblaze.pages.components;

import com.example.demoblaze.config.ConfigManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Dedicated helper for managing JavaScript alerts displayed by DemoBlaze.
 *
 * <p>DemoBlaze relies on native JavaScript window.alert() popups for action
 * feedback (sign-up, login errors, cart operations, checkout validation).
 */
public class BrowserAlertHandler {

    private static final Logger log = LoggerFactory.getLogger(BrowserAlertHandler.class);
    private final WebDriver driver;
    private final int timeoutSeconds;

    public BrowserAlertHandler(WebDriver driver) {
        this.driver = driver;
        this.timeoutSeconds = ConfigManager.getInstance().getExplicitWaitSeconds();
    }

    public Alert waitForAlert() {
        return waitForAlert(timeoutSeconds);
    }

    public Alert waitForAlert(int customTimeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(customTimeoutSeconds))
                .until(ExpectedConditions.alertIsPresent());
    }

    public String acceptAlert() {
        try {
            Alert alert = waitForAlert();
            String text = alert.getText();
            alert.accept();
            log.info("Accepted alert: '{}'", text);
            return text;
        } catch (TimeoutException e) {
            log.warn("Timed out waiting for alert to appear.");
            return "";
        }
    }

    public String dismissAlert() {
        try {
            Alert alert = waitForAlert();
            String text = alert.getText();
            alert.dismiss();
            log.info("Dismissed alert: '{}'", text);
            return text;
        } catch (TimeoutException e) {
            log.warn("Timed out waiting for alert to appear.");
            return "";
        }
    }

    public String getAlertText() {
        try {
            Alert alert = waitForAlert();
            return alert.getText();
        } catch (TimeoutException | NoAlertPresentException e) {
            return "";
        }
    }

    public boolean isAlertPresent(int waitSeconds) {
        try {
            waitForAlert(waitSeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
