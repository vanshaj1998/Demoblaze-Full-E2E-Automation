package com.example.demoblaze.tests.regression;

import com.example.demoblaze.assertions.CartAssertions;
import com.example.demoblaze.base.BaseTest;
import com.example.demoblaze.pages.CartPage;
import com.example.demoblaze.pages.HomePage;
import com.example.demoblaze.pages.ProductPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("DemoBlaze UI Automation")
@Feature("Shopping Cart Operations")
public class CartTests extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver());
    }

    @Test(
            groups = {"regression"},
            description = "DB-CART-001: Add product to cart and verify it appears in cart table"
    )
    @Story("Cart Item Addition")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddProductAndVerifyCartItem() {
        homePage.open();
        ProductPage productPage = homePage.openFirstProduct();
        String productName = productPage.getProductName();
        productPage.addCurrentProductToCart();

        CartPage cartPage = homePage.getNavigationBar().clickCart();
        CartAssertions.assertProductInCart(cartPage, productName);
    }

    @Test(
            groups = {"regression"},
            description = "DB-CART-002: Add multiple items and verify total calculation matches sum of prices"
    )
    @Story("Cart Total Calculation")
    @Severity(SeverityLevel.CRITICAL)
    public void testMultipleItemsCartTotalCalculation() {
        homePage.open();

        // Add first product
        ProductPage product1 = homePage.openProduct("Samsung galaxy s6");
        product1.addCurrentProductToCart();

        // Return home and add second product
        homePage.getNavigationBar().clickHome();
        homePage.open();
        ProductPage product2 = homePage.openProduct("Nokia lumia 1520");
        product2.addCurrentProductToCart();

        CartPage cartPage = homePage.getNavigationBar().clickCart();
        Assert.assertTrue(cartPage.getCartItemCount() >= 2,
                "Expected at least 2 items in cart.");
        CartAssertions.assertCartTotalMatchesItemSum(cartPage);
    }

    @Test(
            groups = {"regression"},
            description = "DB-CART-003: Remove product from cart and verify item count decreases"
    )
    @Story("Cart Item Removal")
    @Severity(SeverityLevel.CRITICAL)
    public void testRemoveProductFromCart() {
        homePage.open();
        ProductPage productPage = homePage.openProduct("Samsung galaxy s6");
        String productName = productPage.getProductName();
        productPage.addCurrentProductToCart();

        CartPage cartPage = homePage.getNavigationBar().clickCart();
        CartAssertions.assertProductInCart(cartPage, productName);

        cartPage.removeProductFromCart(productName);
        CartAssertions.assertProductNotInCart(cartPage, productName);
    }
}
