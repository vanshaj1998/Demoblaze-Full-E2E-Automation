package com.example.demoblaze.assertions;

import com.example.demoblaze.pages.CartPage;
import org.testng.Assert;

import java.util.List;

/**
 * Domain-specific assertions for the shopping cart.
 * Separates verification logic from Page Object interaction mechanics.
 */
public final class CartAssertions {

    private CartAssertions() {}

    /**
     * Verifies that the given product name appears in the cart item list.
     */
    public static void assertProductInCart(CartPage cartPage, String expectedProductName) {
        List<String> products = cartPage.getCartProductNames();
        Assert.assertTrue(
                products.stream().anyMatch(p -> p.equalsIgnoreCase(expectedProductName)),
                String.format("Expected product '%s' to be present in cart. Actual products: %s",
                        expectedProductName, products));
    }

    /**
     * Verifies that the given product name does NOT appear in the cart.
     */
    public static void assertProductNotInCart(CartPage cartPage, String unexpectedProductName) {
        List<String> products = cartPage.getCartProductNames();
        Assert.assertFalse(
                products.stream().anyMatch(p -> p.equalsIgnoreCase(unexpectedProductName)),
                String.format("Expected product '%s' to NOT be in cart, but it was found. Current products: %s",
                        unexpectedProductName, products));
    }

    /**
     * Verifies that the displayed cart total equals the sum of the individual product prices.
     */
    public static void assertCartTotalMatchesItemSum(CartPage cartPage) {
        List<Double> itemPrices = cartPage.getProductPrices();
        double expectedSum = itemPrices.stream().mapToDouble(Double::doubleValue).sum();
        double actualTotal = cartPage.getCartTotal();

        Assert.assertEquals(actualTotal, expectedSum, 0.01,
                String.format("Cart total (%.2f) does not match sum of individual items (%.2f). Items: %s",
                        actualTotal, expectedSum, itemPrices));
    }

    /**
     * Verifies that the cart total matches an expected numeric value.
     */
    public static void assertCartTotalEquals(CartPage cartPage, double expectedTotal) {
        double actualTotal = cartPage.getCartTotal();
        Assert.assertEquals(actualTotal, expectedTotal, 0.01,
                String.format("Expected cart total %.2f but found %.2f", expectedTotal, actualTotal));
    }

    /**
     * Verifies that the cart is empty (zero items displayed).
     */
    public static void assertCartIsEmpty(CartPage cartPage) {
        int count = cartPage.getCartItemCount();
        Assert.assertEquals(count, 0,
                String.format("Expected empty cart, but found %d item(s).", count));
    }
}
