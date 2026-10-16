package com.example.demoblaze.tests.e2e;

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
@Feature("End-to-End Order & Checkout Lifecycle")
public class PurchaseFlowTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(driver());
    }

    @Test(
            groups = {"e2e", "smoke", "regression"},
            description = "DB-E2E-001: Complete e-commerce journey: Browse -> Select -> Add to Cart -> Checkout -> Verify Confirmation"
    )
    @Story("Complete Purchase Flow")
    @Severity(SeverityLevel.BLOCKER)
    public void testCompleteEndToEndPurchaseFlow() {
        // Step 1: Open Home Page
        homePage.open();

        // Step 2: Select a Product
        ProductPage productPage = homePage.openProduct("Samsung galaxy s6");
        String selectedProductName = productPage.getProductName();
        double productPrice = productPage.getNumericPrice();

        // Step 3: Add product to cart
        String alertText = productPage.addCurrentProductToCart();
        Assert.assertTrue(alertText.toLowerCase().contains("product added"),
                "Expected 'Product added' confirmation alert.");

        // Step 4: Open Cart
        CartPage cartPage = homePage.getNavigationBar().clickCart();

        // Step 5: Verify item in cart and check total
        CartAssertions.assertProductInCart(cartPage, selectedProductName);
        CartAssertions.assertCartTotalEquals(cartPage, productPrice);

        // Step 6 & 7: Proceed to Checkout and submit order
        PlaceOrderModal orderModal = cartPage.clickPlaceOrder();
        CustomerData customer = TestDataFactory.validCustomer();
        orderModal.submitOrder(customer);

        // Step 8: Verify purchase confirmation
        Assert.assertTrue(orderModal.isConfirmationDisplayed(),
                "Purchase confirmation dialog should be displayed.");
        Assert.assertEquals(orderModal.getConfirmationHeading(), "Thank you for your purchase!",
                "Confirmation heading does not match expected text.");

        String details = orderModal.getConfirmationDetails();
        log.info("Purchase confirmation details:\n{}", details);
        Assert.assertTrue(details.contains("Id:"), "Confirmation should include an Order Id.");
        Assert.assertTrue(details.contains(customer.name()),
                "Confirmation should reflect customer name: " + customer.name());

        // Step 9: Close confirmation and ensure app remains usable
        orderModal.confirmPurchaseAndClose();
        Assert.assertTrue(homePage.getNavigationBar().isBrandDisplayed(),
                "Application navbar should remain accessible after closing order confirmation.");
    }

    @Test(
            groups = {"regression"},
            description = "DB-E2E-002: Checkout rejected when mandatory fields (Name, Credit Card) are blank"
    )
    @Story("Checkout Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testCheckoutWithMissingMandatoryFields() {
        homePage.open();
        ProductPage productPage = homePage.openFirstProduct();
        productPage.addCurrentProductToCart();

        CartPage cartPage = homePage.getNavigationBar().clickCart();
        PlaceOrderModal orderModal = cartPage.clickPlaceOrder();

        CustomerData invalidCustomer = TestDataFactory.customerWithBlankRequiredFields();
        String alertText = orderModal.submitOrderExpectingAlert(invalidCustomer);

        Assert.assertEquals(alertText, "Please fill out Name and Creditcard.",
                "Expected validation alert when submitting order with empty Name and Card fields.");
    }

    @Test(
            groups = {"e2e", "regression"},
            description = "DB-E2E-003: Login then purchase flow with authenticated user"
    )
    @Story("Authenticated Purchase")
    @Severity(SeverityLevel.CRITICAL)
    public void testAuthenticatedPurchaseFlow() {
        // Register and login
        var testUser = TestDataFactory.uniqueUser();
        homePage.open();
        homePage.getNavigationBar().clickSignup().registerUser(testUser);
        homePage.getNavigationBar().clickLogin().loginAs(testUser);

        // Browse and add product
        homePage.open();
        ProductPage productPage = homePage.openProduct("Nokia lumia 1520");
        productPage.addCurrentProductToCart();

        // Checkout
        CartPage cartPage = homePage.getNavigationBar().clickCart();
        PlaceOrderModal orderModal = cartPage.clickPlaceOrder();
        orderModal.submitOrder(TestDataFactory.validCustomer());

        Assert.assertTrue(orderModal.isConfirmationDisplayed(),
                "Confirmation dialog should appear for authenticated purchase.");
        Assert.assertEquals(orderModal.getConfirmationHeading(), "Thank you for your purchase!");
    }

    @Test(
            groups = {"e2e", "regression"},
            description = "DB-E2E-004: Purchase laptop category product end-to-end"
    )
    @Story("Laptop Purchase Flow")
    @Severity(SeverityLevel.CRITICAL)
    public void testLaptopPurchaseFlow() {
        homePage.open();
        homePage.openCategory("laptop");

        ProductPage productPage = homePage.openFirstProduct();
        String productName = productPage.getProductName();
        productPage.addCurrentProductToCart();

        CartPage cartPage = homePage.getNavigationBar().clickCart();
        CartAssertions.assertProductInCart(cartPage, productName);

        PlaceOrderModal orderModal = cartPage.clickPlaceOrder();
        orderModal.submitOrder(TestDataFactory.validCustomer());

        Assert.assertTrue(orderModal.isConfirmationDisplayed(),
                "Purchase confirmation should be displayed for laptop purchase.");
        orderModal.confirmPurchaseAndClose();
    }

    @Test(
            groups = {"e2e", "regression"},
            description = "DB-E2E-005: Multi-product checkout with total verification"
    )
    @Story("Multi-Product Purchase")
    @Severity(SeverityLevel.CRITICAL)
    public void testMultiProductPurchaseFlow() {
        homePage.open();

        // Add first product
        ProductPage product1 = homePage.openProduct("Samsung galaxy s6");
        double price1 = product1.getNumericPrice();
        product1.addCurrentProductToCart();

        // Return home and add second
        homePage.getNavigationBar().clickHome();
        homePage.open();
        ProductPage product2 = homePage.openProduct("Nokia lumia 1520");
        double price2 = product2.getNumericPrice();
        product2.addCurrentProductToCart();

        // Verify cart
        CartPage cartPage = homePage.getNavigationBar().clickCart();
        cartPage.waitForCartItemsCount(2, 10);
        Assert.assertTrue(cartPage.getCartItemCount() >= 2,
                "Cart should contain at least 2 items.");
        CartAssertions.assertCartTotalMatchesItemSum(cartPage);

        // Checkout
        PlaceOrderModal orderModal = cartPage.clickPlaceOrder();
        orderModal.submitOrder(TestDataFactory.validCustomer());

        Assert.assertTrue(orderModal.isConfirmationDisplayed(),
                "Purchase confirmation should appear for multi-product order.");
        orderModal.confirmPurchaseAndClose();
    }
}
