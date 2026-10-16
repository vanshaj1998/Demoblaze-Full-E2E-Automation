package com.example.demoblaze.tests.regression;

import com.example.demoblaze.assertions.CartAssertions;
import com.example.demoblaze.base.BaseTest;
import com.example.demoblaze.data.TestDataFactory;
import com.example.demoblaze.models.CustomerData;
import com.example.demoblaze.pages.CartPage;
import com.example.demoblaze.pages.HomePage;
import com.example.demoblaze.pages.ProductPage;
import com.example.demoblaze.pages.components.PlaceOrderModal;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("DemoBlaze UI Automation")
@Feature("Checkout & Order Validation")
public class CheckoutTests extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver());
    }

    @Test(
            groups = {"regression"},
            description = "DB-CHKOUT-001: Checkout with only name filled shows validation alert"
    )
    @Story("Checkout Validation - Missing Card")
    @Severity(SeverityLevel.NORMAL)
    public void testCheckoutWithOnlyNameFilled() {
        homePage.open();
        ProductPage productPage = homePage.openFirstProduct();
        productPage.addCurrentProductToCart();

        CartPage cartPage = homePage.getNavigationBar().clickCart();
        PlaceOrderModal orderModal = cartPage.clickPlaceOrder();

        CustomerData customer = TestDataFactory.customerWithOnlyName();
        String alertText = orderModal.submitOrderExpectingAlert(customer);

        Assert.assertEquals(alertText, "Please fill out Name and Creditcard.",
                "Expected validation alert when credit card is missing.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-CHKOUT-002: Checkout with only card filled shows validation alert"
    )
    @Story("Checkout Validation - Missing Name")
    @Severity(SeverityLevel.NORMAL)
    public void testCheckoutWithOnlyCardFilled() {
        homePage.open();
        ProductPage productPage = homePage.openFirstProduct();
        productPage.addCurrentProductToCart();

        CartPage cartPage = homePage.getNavigationBar().clickCart();
        PlaceOrderModal orderModal = cartPage.clickPlaceOrder();

        CustomerData customer = TestDataFactory.customerWithOnlyCard();
        String alertText = orderModal.submitOrderExpectingAlert(customer);

        Assert.assertEquals(alertText, "Please fill out Name and Creditcard.",
                "Expected validation alert when name is missing.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-CHKOUT-003: Place Order modal displays cart total"
    )
    @Story("Checkout Modal Total")
    @Severity(SeverityLevel.NORMAL)
    public void testPlaceOrderModalShowsTotal() {
        homePage.open();
        ProductPage productPage = homePage.openProduct("Samsung galaxy s6");
        productPage.addCurrentProductToCart();

        CartPage cartPage = homePage.getNavigationBar().clickCart();
        PlaceOrderModal orderModal = cartPage.clickPlaceOrder();

        String totalText = orderModal.getModalTotalText();
        Assert.assertFalse(totalText.isBlank(),
                "Place Order modal should display a total amount.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-CHKOUT-004: Place Order modal can be closed without purchasing"
    )
    @Story("Checkout Modal Close")
    @Severity(SeverityLevel.NORMAL)
    public void testCloseOrderModalWithoutPurchasing() {
        homePage.open();
        ProductPage productPage = homePage.openFirstProduct();
        productPage.addCurrentProductToCart();

        CartPage cartPage = homePage.getNavigationBar().clickCart();
        PlaceOrderModal orderModal = cartPage.clickPlaceOrder();
        Assert.assertTrue(orderModal.isDisplayed(),
                "Place Order modal should be displayed.");

        orderModal.close();
        // Cart page should still be accessible
        Assert.assertTrue(cartPage.getCartItemCount() > 0,
                "Cart items should still be present after closing modal.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-CHKOUT-005: Successful purchase with partial optional fields"
    )
    @Story("Checkout with Minimal Required Fields")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulPurchaseWithMinimalFields() {
        homePage.open();
        ProductPage productPage = homePage.openFirstProduct();
        productPage.addCurrentProductToCart();

        CartPage cartPage = homePage.getNavigationBar().clickCart();
        PlaceOrderModal orderModal = cartPage.clickPlaceOrder();

        // Only name and card are required by DemoBlaze
        CustomerData customer = TestDataFactory.customerPartialOptionalOnly();
        orderModal.submitOrder(customer);

        Assert.assertTrue(orderModal.isConfirmationDisplayed(),
                "Purchase confirmation should be displayed with minimal required fields.");
        Assert.assertEquals(orderModal.getConfirmationHeading(), "Thank you for your purchase!",
                "Confirmation heading should match.");
    }
}
