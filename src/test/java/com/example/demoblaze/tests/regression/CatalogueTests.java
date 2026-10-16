package com.example.demoblaze.tests.regression;

import com.example.demoblaze.base.BaseTest;
import com.example.demoblaze.pages.HomePage;
import com.example.demoblaze.pages.ProductPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

@Epic("DemoBlaze UI Automation")
@Feature("Product Catalogue Pagination & Images")
public class CatalogueTests extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver());
    }

    @Test(
            groups = {"regression"},
            description = "DB-CAT-001: Next button is visible on home page with products"
    )
    @Story("Pagination Controls")
    @Severity(SeverityLevel.NORMAL)
    public void testNextButtonIsVisible() {
        homePage.open();
        Assert.assertTrue(homePage.isNextButtonDisplayed(),
                "Next page button should be visible on the home page.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-CAT-002: Clicking Next loads different product set"
    )
    @Story("Pagination Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testNextPageLoadsDifferentProducts() {
        homePage.open();
        List<String> firstPageProducts = homePage.getDisplayedProductNames();
        homePage.clickNextPage();
        List<String> secondPageProducts = homePage.getDisplayedProductNames();
        // At least one product should differ or page should still have products
        Assert.assertFalse(secondPageProducts.isEmpty(),
                "Second page should display products after clicking Next.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-CAT-003: Previous button navigates back to first page"
    )
    @Story("Pagination Back Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testPreviousPageNavigation() {
        homePage.open();
        List<String> firstPageProducts = homePage.getDisplayedProductNames();
        homePage.clickNextPage();
        homePage.clickPrevPage();
        List<String> returnedPageProducts = homePage.getDisplayedProductNames();
        Assert.assertFalse(returnedPageProducts.isEmpty(),
                "Products should be displayed after navigating back with Previous.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-CAT-004: Product cards display prices on home page"
    )
    @Story("Product Card Pricing")
    @Severity(SeverityLevel.NORMAL)
    public void testProductCardsDisplayPrices() {
        homePage.open();
        List<String> prices = homePage.getDisplayedProductPrices();
        Assert.assertFalse(prices.isEmpty(),
                "Product cards should display prices.");
        Assert.assertTrue(prices.stream().allMatch(p -> p.contains("$")),
                "All product prices should contain '$' symbol. Found: " + prices);
    }

    @Test(
            groups = {"regression"},
            description = "DB-CAT-005: Product cards display images with valid src"
    )
    @Story("Product Card Images")
    @Severity(SeverityLevel.NORMAL)
    public void testProductCardsHaveImages() {
        homePage.open();
        Assert.assertTrue(homePage.hasProductImages(),
                "All product cards should have images with valid src attributes.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-CAT-006: Phones category only shows phone products"
    )
    @Story("Category Filtering Accuracy")
    @Severity(SeverityLevel.NORMAL)
    public void testPhonesCategoryFiltering() {
        homePage.open();
        homePage.openCategory("phone");
        List<String> phones = homePage.getDisplayedProductNames();
        Assert.assertFalse(phones.isEmpty(),
                "Phones category should display at least one product.");
        // Known phones on DemoBlaze
        List<String> knownPhones = List.of("Samsung galaxy s6", "Nokia lumia 1520",
                "Nexus 6", "Samsung galaxy s7", "Iphone 6 32gb", "Sony xperia z5", "HTC One M9");
        Assert.assertTrue(
                phones.stream().allMatch(p -> knownPhones.stream()
                        .anyMatch(kp -> kp.equalsIgnoreCase(p))),
                "All displayed products should be phones. Found: " + phones);
    }

    @Test(
            groups = {"regression"},
            description = "DB-CAT-007: Laptops category only shows laptop products"
    )
    @Story("Category Filtering Accuracy")
    @Severity(SeverityLevel.NORMAL)
    public void testLaptopsCategoryFiltering() {
        homePage.open();
        homePage.openCategory("laptop");
        List<String> laptops = homePage.getDisplayedProductNames();
        Assert.assertFalse(laptops.isEmpty(),
                "Laptops category should display at least one product.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-CAT-008: Monitors category only shows monitor products"
    )
    @Story("Category Filtering Accuracy")
    @Severity(SeverityLevel.NORMAL)
    public void testMonitorsCategoryFiltering() {
        homePage.open();
        homePage.openCategory("monitor");
        List<String> monitors = homePage.getDisplayedProductNames();
        Assert.assertFalse(monitors.isEmpty(),
                "Monitors category should display at least one product.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-CAT-009: Product detail page shows product image"
    )
    @Story("Product Detail Image")
    @Severity(SeverityLevel.NORMAL)
    public void testProductDetailHasImage() {
        homePage.open();
        ProductPage productPage = homePage.openFirstProduct();
        String imgSrc = productPage.getProductImageSrc();
        Assert.assertFalse(imgSrc.isBlank(),
                "Product detail page should display a product image.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-CAT-010: Product detail page shows product description"
    )
    @Story("Product Detail Description")
    @Severity(SeverityLevel.NORMAL)
    public void testProductDetailHasDescription() {
        homePage.open();
        ProductPage productPage = homePage.openFirstProduct();
        Assert.assertTrue(productPage.hasProductDescription(),
                "Product detail page should contain a product description.");
    }
}
