package com.example.demoblaze.tests.regression;

import com.example.demoblaze.base.BaseTest;
import com.example.demoblaze.pages.HomePage;
import com.example.demoblaze.pages.components.NavigationBar;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("DemoBlaze UI Automation")
@Feature("Navigation & UI Structure")
public class NavigationTests extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver());
    }

    @Test(
            groups = {"regression"},
            description = "DB-NAV-005: Clicking brand logo navigates to home page"
    )
    @Story("Brand Logo Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testBrandLogoNavigatesToHome() {
        homePage.open();
        // Navigate to cart first
        homePage.getNavigationBar().clickCart();
        // Click brand logo
        homePage.getNavigationBar().clickBrand();
        Assert.assertTrue(driver().getCurrentUrl().contains("demoblaze.com"),
                "Brand logo should navigate back to home page.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-NAV-006: Verify Contact link is visible in navigation bar"
    )
    @Story("Contact Link Visibility")
    @Severity(SeverityLevel.NORMAL)
    public void testContactLinkVisible() {
        homePage.open();
        NavigationBar nav = homePage.getNavigationBar();
        Assert.assertTrue(nav.isContactLinkDisplayed(),
                "Contact link should be visible in the navigation bar.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-NAV-007: Verify About Us link is visible in navigation bar"
    )
    @Story("About Link Visibility")
    @Severity(SeverityLevel.NORMAL)
    public void testAboutUsLinkVisible() {
        homePage.open();
        NavigationBar nav = homePage.getNavigationBar();
        Assert.assertTrue(nav.isAboutLinkDisplayed(),
                "About Us link should be visible in the navigation bar.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-NAV-008: Contact modal opens when Contact link is clicked"
    )
    @Story("Contact Modal")
    @Severity(SeverityLevel.NORMAL)
    public void testContactModalOpens() {
        homePage.open();
        homePage.getNavigationBar().clickContact();
        Assert.assertTrue(homePage.isContactModalDisplayed(),
                "Contact modal should be displayed after clicking Contact link.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-NAV-009: About Us modal opens when About Us link is clicked"
    )
    @Story("About Us Modal")
    @Severity(SeverityLevel.NORMAL)
    public void testAboutUsModalOpens() {
        homePage.open();
        homePage.getNavigationBar().clickAboutUs();
        Assert.assertTrue(homePage.isAboutModalDisplayed(),
                "About Us modal should be displayed after clicking About Us link.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-NAV-010: Page title contains STORE"
    )
    @Story("Page Title")
    @Severity(SeverityLevel.MINOR)
    public void testPageTitleContainsStore() {
        homePage.open();
        String title = homePage.getPageTitle();
        Assert.assertTrue(title.toLowerCase().contains("store"),
                "Page title should contain 'STORE'. Actual: " + title);
    }

    @Test(
            groups = {"regression"},
            description = "DB-NAV-011: Footer section is displayed on home page"
    )
    @Story("Footer Visibility")
    @Severity(SeverityLevel.MINOR)
    public void testFooterIsDisplayed() {
        homePage.open();
        Assert.assertTrue(homePage.isFooterDisplayed(),
                "Footer section should be visible on the home page.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-NAV-012: Home link navigates back from cart page to home"
    )
    @Story("Home Link Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testHomeLinkNavigatesFromCart() {
        homePage.open();
        homePage.getNavigationBar().clickCart();
        homePage.getNavigationBar().clickHome();
        homePage.waitForProducts();
        int productCount = homePage.getProductCount();
        Assert.assertTrue(productCount > 0,
                "Home link should navigate back and display products.");
    }
}
