package com.example.demoblaze.tests.regression;

import com.example.demoblaze.base.BaseTest;
import com.example.demoblaze.data.TestDataFactory;
import com.example.demoblaze.models.UserData;
import com.example.demoblaze.pages.HomePage;
import com.example.demoblaze.pages.components.LoginModal;
import com.example.demoblaze.pages.components.NavigationBar;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("DemoBlaze UI Automation")
@Feature("Authentication & Session Management")
public class LoginTests extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver());
    }

    @Test(
            groups = {"regression"},
            description = "DB-LOGIN-001: Login successfully with registered credentials and verify user greeting"
    )
    @Story("Positive Login")
    @Severity(SeverityLevel.BLOCKER)
    public void testSuccessfulLogin() {
        // Register a fresh dedicated user first to ensure test isolation
        UserData testUser = TestDataFactory.uniqueUser();
        homePage.open();
        homePage.getNavigationBar().clickSignup().registerUser(testUser);

        // Perform login with the newly created user
        NavigationBar nav = homePage.getNavigationBar();
        LoginModal loginModal = nav.clickLogin();
        loginModal.loginAs(testUser);

        Assert.assertTrue(nav.isUserGreetingDisplayed(), "User greeting should be displayed after login.");
        Assert.assertTrue(nav.getUserGreetingText().contains(testUser.username()),
                "Greeting should contain username: " + testUser.username());
        Assert.assertTrue(nav.isLogoutLinkDisplayed(), "Logout link should appear after login.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-LOGIN-002: Login rejected with wrong password"
    )
    @Story("Negative Login - Password Mismatch")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithWrongPassword() {
        homePage.open();
        LoginModal modal = homePage.getNavigationBar().clickLogin();

        UserData invalidUser = TestDataFactory.userWithWrongPassword();
        String alertText = modal.loginExpectingAlert(invalidUser);

        Assert.assertEquals(alertText, "Wrong password.",
                "Expected 'Wrong password.' alert for incorrect credentials.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-LOGIN-003: Login rejected for non-existent user"
    )
    @Story("Negative Login - Unknown User")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithNonExistentUser() {
        homePage.open();
        LoginModal modal = homePage.getNavigationBar().clickLogin();

        UserData nonExistent = TestDataFactory.uniqueUser();
        String alertText = modal.loginExpectingAlert(nonExistent);

        Assert.assertEquals(alertText, "User does not exist.",
                "Expected 'User does not exist.' alert for unregistered user.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-LOGIN-004: Login rejected when credentials are blank"
    )
    @Story("Negative Login - Form Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithBlankCredentials() {
        homePage.open();
        LoginModal modal = homePage.getNavigationBar().clickLogin();

        UserData blankUser = TestDataFactory.userWithBlankUsername();
        String alertText = modal.loginExpectingAlert(blankUser);

        Assert.assertEquals(alertText, "Please fill out Username and Password.",
                "Expected validation alert when logging in with blank fields.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-LOGIN-005: User can log out and session state resets"
    )
    @Story("Session Management - Logout")
    @Severity(SeverityLevel.CRITICAL)
    public void testUserLogout() {
        // Register and login
        UserData user = TestDataFactory.uniqueUser();
        homePage.open();
        homePage.getNavigationBar().clickSignup().registerUser(user);

        NavigationBar nav = homePage.getNavigationBar();
        nav.clickLogin().loginAs(user);
        Assert.assertTrue(nav.isLogoutLinkDisplayed(), "Logout button should be present.");

        // Logout
        nav.clickLogout();

        Assert.assertTrue(nav.isLoginLinkDisplayed(), "Login link should re-appear after logout.");
        Assert.assertTrue(nav.isSignupLinkDisplayed(), "Sign up link should re-appear after logout.");
    }
}
