package com.example.demoblaze.tests.smoke;

import com.example.demoblaze.base.BaseTest;
import com.example.demoblaze.pages.HomePage;
import com.example.demoblaze.pages.components.NavigationBar;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

@Epic("DemoBlaze UI Automation")
@Feature("Home Page & Navigation")
public class HomePageSmokeTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver());
    }

    @Test(
            groups = {"smoke", "regression"},
            description = "DB-NAV-001: Verify home page loads and displays essential navigation elements"
    )
    @Story("Navigation Visibility")
    @Severity(SeverityLevel.BLOCKER)
    public void testHomePageAndNavElementsVisible() {
        homePage.open();

        NavigationBar nav = homePage.getNavigationBar();
        Assert.assertTrue(nav.isBrandDisplayed(), "Brand logo should be visible on navbar.");
        Assert.assertTrue(nav.isHomeLinkDisplayed(), "Home link should be visible on navbar.");
        Assert.assertTrue(nav.isCartLinkDisplayed(), "Cart link should be visible on navbar.");
        Assert.assertTrue(nav.isLoginLinkDisplayed(), "Log in link should be visible on navbar.");
        Assert.assertTrue(nav.isSignupLinkDisplayed(), "Sign up link should be visible on navbar.");
    }

    @Test(
            groups = {"smoke", "regression"},
            description = "DB-NAV-002: Verify home page hero carousel is displayed"
    )
    @Story("Home Page Visuals")
    @Severity(SeverityLevel.NORMAL)
    public void testHeroCarouselDisplayed() {
        homePage.open();
        Assert.assertTrue(homePage.isCarouselDisplayed(), "Carousel should be visible on the home page.");
    }

    @Test(
            groups = {"smoke", "regression"},
            description = "DB-NAV-003: Verify product catalogue populates on initial load"
    )
    @Story("Product Catalogue")
    @Severity(SeverityLevel.CRITICAL)
    public void testProductCatalogueLoads() {
        homePage.open();
        int productCount = homePage.getProductCount();
        Assert.assertTrue(productCount > 0,
                "Expected at least one product on home page, but found: " + productCount);
    }

    @Test(
            groups = {"smoke", "regression"},
            description = "DB-NAV-004: Verify category sidebar options are available"
    )
    @Story("Category Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testCategorySidebarAvailable() {
        homePage.open();
        List<String> categories = homePage.getAvailableCategories();
        Assert.assertTrue(categories.stream().anyMatch(c -> c.toLowerCase().contains("phone")),
                "Phones category should be present in sidebar. Found: " + categories);
        Assert.assertTrue(categories.stream().anyMatch(c -> c.toLowerCase().contains("laptop")),
                "Laptops category should be present in sidebar. Found: " + categories);
        Assert.assertTrue(categories.stream().anyMatch(c -> c.toLowerCase().contains("monitor")),
                "Monitors category should be present in sidebar. Found: " + categories);
    }
}
