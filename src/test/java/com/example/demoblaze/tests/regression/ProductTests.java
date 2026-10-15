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
@Feature("Product Catalogue & Details")
public class ProductTests extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver());
    }

    @Test(
            groups = {"regression"},
            description = "DB-PROD-001: Filter catalogue by category (Phones, Laptops, Monitors)"
    )
    @Story("Category Filtering")
    @Severity(SeverityLevel.NORMAL)
    public void testCategoryFiltering() {
        homePage.open();

        // 1. Phones
        homePage.openCategory("phone");
        List<String> phones = homePage.getDisplayedProductNames();
        Assert.assertFalse(phones.isEmpty(), "Phones category should display products.");

        // 2. Laptops
        homePage.openCategory("laptop");
        List<String> laptops = homePage.getDisplayedProductNames();
        Assert.assertFalse(laptops.isEmpty(), "Laptops category should display products.");

        // 3. Monitors
        homePage.openCategory("monitor");
        List<String> monitors = homePage.getDisplayedProductNames();
        Assert.assertFalse(monitors.isEmpty(), "Monitors category should display products.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-PROD-002: View product details and verify specifications and price"
    )
    @Story("Product Detail Verification")
    @Severity(SeverityLevel.CRITICAL)
    public void testProductDetailsDisplay() {
        homePage.open();
        ProductPage productPage = homePage.openFirstProduct();

        Assert.assertFalse(productPage.getProductName().isBlank(),
                "Product name should not be blank.");
        Assert.assertTrue(productPage.getNumericPrice() > 0,
                "Product price should be greater than zero.");
        Assert.assertTrue(productPage.isAddToCartButtonDisplayed(),
                "'Add to cart' button must be displayed.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-PROD-003: Add product to shopping cart and verify confirmation alert"
    )
    @Story("Add to Cart")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddProductToCart() {
        homePage.open();
        ProductPage productPage = homePage.openFirstProduct();

        String alertText = productPage.addCurrentProductToCart();
        Assert.assertTrue(alertText.toLowerCase().contains("product added"),
                "Expected alert containing 'Product added' but got: " + alertText);
    }
}
