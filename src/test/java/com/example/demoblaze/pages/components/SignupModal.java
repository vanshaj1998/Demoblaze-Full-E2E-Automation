package com.example.demoblaze.pages.components;

import com.example.demoblaze.models.UserData;
import com.example.demoblaze.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Modal component for user registration (#signInModal).
 */
public class SignupModal extends BasePage {

    private static final By MODAL_CONTAINER = By.id("signInModal");
    private static final By USERNAME_INPUT  = By.id("sign-username");
    private static final By PASSWORD_INPUT  = By.id("sign-password");
    private static final By SIGNUP_BUTTON   = By.xpath("//button[@onclick='register()']");
    private static final By CLOSE_BUTTON    = By.cssSelector("#signInModal .btn-secondary");

    private final BrowserAlertHandler alertHandler;

    public SignupModal(WebDriver driver) {
        super(driver);
        this.alertHandler = new BrowserAlertHandler(driver);
    }

    public boolean isDisplayed() {
        return isDisplayed(MODAL_CONTAINER);
    }

    public SignupModal enterUsername(String username) {
        type(USERNAME_INPUT, username);
        return this;
    }

    public SignupModal enterPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    public void clickSignUp() {
        click(SIGNUP_BUTTON);
    }

    /**
     * Fills out the registration form, clicks Sign up, and captures the alert text.
     *
     * @param user UserData containing credentials
     * @return alert text (e.g. "Sign up successful." or "This user already exist.")
     */
    public String registerUser(UserData user) {
        log.info("Registering user '{}'", user.username());
        enterUsername(user.username());
        enterPassword(user.password());
        clickSignUp();
        String alertText = alertHandler.acceptAlert();
        log.info("Registration alert response: '{}'", alertText);
        return alertText;
    }

    public void close() {
        click(CLOSE_BUTTON);
        waitForInvisibility(MODAL_CONTAINER);
    }
}
