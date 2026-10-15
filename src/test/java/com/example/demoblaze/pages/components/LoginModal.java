package com.example.demoblaze.pages.components;

import com.example.demoblaze.models.UserData;
import com.example.demoblaze.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Modal component for user login (#logInModal).
 */
public class LoginModal extends BasePage {

    private static final By MODAL_CONTAINER = By.id("logInModal");
    private static final By USERNAME_INPUT  = By.id("loginusername");
    private static final By PASSWORD_INPUT  = By.id("loginpassword");
    private static final By LOGIN_BUTTON    = By.xpath("//button[@onclick='logIn()']");
    private static final By CLOSE_BUTTON    = By.cssSelector("#logInModal .btn-secondary");

    private final BrowserAlertHandler alertHandler;

    public LoginModal(WebDriver driver) {
        super(driver);
        this.alertHandler = new BrowserAlertHandler(driver);
    }

    public boolean isDisplayed() {
        return isDisplayed(MODAL_CONTAINER);
    }

    public LoginModal enterUsername(String username) {
        type(USERNAME_INPUT, username);
        return this;
    }

    public LoginModal enterPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    public void clickLogin() {
        click(LOGIN_BUTTON);
    }

    public void loginAs(UserData user) {
        log.info("Logging in as '{}'", user.username());
        enterUsername(user.username());
        enterPassword(user.password());
        clickLogin();
    }

    /**
     * Attempts login expecting an alert popup (negative case).
     *
     * @return alert message text
     */
    public String loginExpectingAlert(UserData user) {
        loginAs(user);
        return alertHandler.acceptAlert();
    }

    public void close() {
        click(CLOSE_BUTTON);
        waitForInvisibility(MODAL_CONTAINER);
    }
}
