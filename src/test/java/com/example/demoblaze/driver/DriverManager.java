package com.example.demoblaze.driver;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe holder for the active WebDriver instance.
 *
 * <p>Each test thread obtains its own driver via {@link #getDriver()}.
 * BaseTest is responsible for calling {@link #createDriver()} before tests
 * and {@link #quitDriver()} afterwards, even on failure.
 *
 * <p>Using ThreadLocal prevents cross-test state leakage during test execution.
 */
public final class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);

    // ThreadLocal ensures each test thread has its own isolated driver instance.
    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() { /* utility class */ }

    /**
     * Creates and stores a new WebDriver for the current thread.
     */
    public static void createDriver() {
        if (DRIVER_THREAD_LOCAL.get() != null) {
            log.warn("A driver already exists for this thread – quitting the old one first.");
            quitDriver();
        }
        WebDriver driver = DriverFactory.createDriver();
        DRIVER_THREAD_LOCAL.set(driver);
        log.debug("Driver stored in ThreadLocal for thread '{}'.", Thread.currentThread().getName());
    }

    /**
     * Returns the WebDriver for the current thread.
     *
     * @throws IllegalStateException if no driver has been created yet
     */
    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "No WebDriver found for thread '" + Thread.currentThread().getName()
                    + "'. Ensure createDriver() is called in @BeforeMethod.");
        }
        return driver;
    }

    /**
     * Quits the current thread's driver and removes it from ThreadLocal.
     * Safe to call even when no driver exists.
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("Driver quit successfully.");
            } catch (Exception e) {
                log.warn("Exception while quitting driver: {}", e.getMessage());
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }

    /** Returns true if a driver instance is present for the current thread. */
    public static boolean hasDriver() {
        return DRIVER_THREAD_LOCAL.get() != null;
    }
}
