package com.example.demoblaze.tests.regression;

import com.example.demoblaze.base.BaseTest;
import com.example.demoblaze.data.TestDataFactory;
import com.example.demoblaze.models.UserData;
import com.example.demoblaze.pages.HomePage;
import com.example.demoblaze.pages.components.SignupModal;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("DemoBlaze UI Automation")
@Feature("User Registration")
public class SignupTests extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver());
    }

    @Test(
            groups = {"regression"},
            description = "DB-SIGNUP-001: Register user successfully with unique synthetic credentials"
    )
    @Story("Positive Registration")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulRegistration() {
        homePage.open();
        SignupModal modal = homePage.getNavigationBar().clickSignup();

        UserData newUser = TestDataFactory.uniqueUser();
        String alertText = modal.registerUser(newUser);

        Assert.assertEquals(alertText, "Sign up successful.",
                "Expected success alert on new user registration.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-SIGNUP-002: Registration rejected with duplicate username"
    )
    @Story("Negative Registration")
    @Severity(SeverityLevel.NORMAL)
    public void testDuplicateUsernameRegistration() {
        homePage.open();
        SignupModal modal = homePage.getNavigationBar().clickSignup();

        // Attempt to register with known existing user
        UserData existingUser = TestDataFactory.existingUser();
        String alertText = modal.registerUser(existingUser);

        Assert.assertEquals(alertText, "This user already exist.",
                "Expected duplicate user alert when re-registering an existing username.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-SIGNUP-003: Registration rejected when required credentials are blank"
    )
    @Story("Negative Registration - Form Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testBlankCredentialsRegistration() {
        homePage.open();
        SignupModal modal = homePage.getNavigationBar().clickSignup();

        UserData blankUser = TestDataFactory.userWithBlankUsername();
        String alertText = modal.registerUser(blankUser);

        Assert.assertEquals(alertText, "Please fill out Username and Password.",
                "Expected validation alert when registering with blank credentials.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-SIGNUP-004: Registration rejected when password is blank"
    )
    @Story("Negative Registration - Blank Password")
    @Severity(SeverityLevel.NORMAL)
    public void testBlankPasswordRegistration() {
        homePage.open();
        SignupModal modal = homePage.getNavigationBar().clickSignup();

        UserData blankPwdUser = TestDataFactory.userWithBlankPassword();
        String alertText = modal.registerUser(blankPwdUser);

        Assert.assertEquals(alertText, "Please fill out Username and Password.",
                "Expected validation alert when registering with blank password.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-SIGNUP-005: Signup modal can be closed via Close button"
    )
    @Story("Signup Modal UI")
    @Severity(SeverityLevel.MINOR)
    public void testSignupModalCanBeClosed() {
        homePage.open();
        SignupModal modal = homePage.getNavigationBar().clickSignup();
        Assert.assertTrue(modal.isDisplayed(), "Signup modal should be displayed.");

        modal.close();
        Assert.assertTrue(homePage.getNavigationBar().isBrandDisplayed(),
                "Brand logo should still be visible after closing signup modal.");
    }
}
