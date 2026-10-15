package com.example.demoblaze.base;

import com.example.demoblaze.config.ConfigManager;
import com.example.demoblaze.driver.DriverManager;
import com.example.demoblaze.listeners.TestListener;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.*;

/**
 * Base class for all test classes.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Create a fresh WebDriver before each test method.</li>
 *   <li>Quit the driver after each test method (even on failure).</li>
 *   <li>Log test lifecycle events.</li>
 *   <li>Register the TestListener.</li>
 * </ul>
 *
 * <p>Each test method gets an independent, isolated WebDriver instance.
 * Tests must not rely on state left by a previous test.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /** Shortcut to the active driver for the current test thread. */
    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestContext context) {
        log.info("------ Setting up test | Thread: {} ------", Thread.currentThread().getName());
        DriverManager.createDriver();
        log.info("Driver ready. BaseURL: {}", ConfigManager.getInstance().getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("------ Tearing down test | Thread: {} ------", Thread.currentThread().getName());
        DriverManager.quitDriver();
    }

    // -------------------------------------------------------------------------
    // Shared step helpers (logged by Allure)
    // -------------------------------------------------------------------------

    @Step("Open the application home page")
    protected void openHomePage() {
        driver().get(ConfigManager.getInstance().getBaseUrl());
    }
}
