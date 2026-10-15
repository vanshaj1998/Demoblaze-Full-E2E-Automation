package com.example.demoblaze.pages;

import com.example.demoblaze.config.ConfigManager;
import com.example.demoblaze.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Base class for all Page Objects.
 *
 * <p>Provides common browser interactions built on top of explicit waits.
 * No test assertions belong here. No driver creation happens here.
 */
public abstract class BasePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final WebDriver driver;
    protected final int waitSeconds;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitSeconds = ConfigManager.getInstance().getExplicitWaitSeconds();
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    protected void navigateTo(String url) {
        log.debug("Navigating to: {}", url);
        driver.get(url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    // -------------------------------------------------------------------------
    // Element retrieval with waits
    // -------------------------------------------------------------------------

    protected WebElement findVisible(By locator) {
        return WaitUtils.waitForVisibility(driver, locator, waitSeconds);
    }

    protected WebElement findClickable(By locator) {
        return WaitUtils.waitForClickability(driver, locator, waitSeconds);
    }

    protected List<WebElement> findAllVisible(By locator) {
        WaitUtils.waitForVisibility(driver, locator, waitSeconds);
        return driver.findElements(locator);
    }

    protected boolean isDisplayed(By locator) {
        try {
            return WaitUtils.waitForVisibility(driver, locator, 5).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // Interactions
    // -------------------------------------------------------------------------

    protected void click(By locator) {
        findClickable(locator).click();
    }

    protected void click(WebElement element) {
        WaitUtils.waitForClickability(driver, element, waitSeconds).click();
    }

    protected void type(By locator, String text) {
        WebElement el = findVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void typeInto(WebElement element, String text) {
        WaitUtils.waitForVisibility(driver, element, waitSeconds);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        try {
            return findVisible(locator).getText().trim();
        } catch (Exception e) {
            log.warn("getText failed for locator '{}': {}", locator, e.getMessage());
            return "";
        }
    }

    protected String getAttribute(By locator, String attribute) {
        return findVisible(locator).getAttribute(attribute);
    }

    // -------------------------------------------------------------------------
    // Wait helpers
    // -------------------------------------------------------------------------

    protected boolean waitForInvisibility(By locator) {
        return WaitUtils.waitForInvisibility(driver, locator, waitSeconds);
    }

    protected void waitForUrlContaining(String fragment) {
        WaitUtils.waitForUrlContains(driver, fragment, waitSeconds);
    }

    protected void waitForProductsToLoad(By rowLocator) {
        WaitUtils.waitForElementsPresent(driver, rowLocator, 1, waitSeconds);
    }

    // -------------------------------------------------------------------------
    // JavaScript helpers
    // -------------------------------------------------------------------------

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }
}
