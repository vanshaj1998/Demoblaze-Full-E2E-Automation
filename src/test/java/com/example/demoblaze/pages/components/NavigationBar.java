package com.example.demoblaze.pages.components;

import com.example.demoblaze.pages.BasePage;
import com.example.demoblaze.pages.CartPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Reusable navigation bar component present across all DemoBlaze pages.
 */
public class NavigationBar extends BasePage {

    private static final By BRAND_LOGO    = By.id("nava");
    private static final By HOME_LINK     = By.xpath("//a[contains(@class,'nav-link') and contains(text(),'Home')]");
    private static final By CART_LINK     = By.id("cartur");
    private static final By LOGIN_LINK    = By.id("login2");
    private static final By SIGNUP_LINK   = By.id("signin2");
    private static final By LOGOUT_LINK   = By.id("logout2");
    private static final By USER_GREETING = By.id("nameofuser");

    public NavigationBar(WebDriver driver) {
        super(driver);
    }

    public boolean isBrandDisplayed() {
        return isDisplayed(BRAND_LOGO);
    }

    public boolean isHomeLinkDisplayed() {
        return isDisplayed(HOME_LINK);
    }

    public boolean isCartLinkDisplayed() {
        return isDisplayed(CART_LINK);
    }

    public boolean isLoginLinkDisplayed() {
        return isDisplayed(LOGIN_LINK);
    }

    public boolean isSignupLinkDisplayed() {
        return isDisplayed(SIGNUP_LINK);
    }

    public boolean isLogoutLinkDisplayed() {
        return isDisplayed(LOGOUT_LINK);
    }

    public boolean isUserGreetingDisplayed() {
        return isDisplayed(USER_GREETING);
    }

    public String getUserGreetingText() {
        return getText(USER_GREETING);
    }

    public LoginModal clickLogin() {
        log.info("Clicking Log In nav button");
        click(LOGIN_LINK);
        return new LoginModal(driver);
    }

    public SignupModal clickSignup() {
        log.info("Clicking Sign Up nav button");
        click(SIGNUP_LINK);
        return new SignupModal(driver);
    }

    public void clickLogout() {
        log.info("Clicking Log Out nav button");
        click(LOGOUT_LINK);
        waitForInvisibility(LOGOUT_LINK);
    }

    public CartPage clickCart() {
        log.info("Clicking Cart nav button");
        click(CART_LINK);
        return new CartPage(driver);
    }

    public void clickHome() {
        log.info("Clicking Home nav button");
        click(HOME_LINK);
    }
}
