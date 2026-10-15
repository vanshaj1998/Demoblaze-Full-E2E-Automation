package com.example.demoblaze.pages;

import com.example.demoblaze.pages.components.BrowserAlertHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the DemoBlaze product detail page (prod.html).
 */
public class ProductPage extends BasePage {

    private static final By PRODUCT_NAME    = By.cssSelector("h2.name");
    private static final By PRODUCT_PRICE   = By.cssSelector("h3.price-container");
    private static final By PRODUCT_DESC    = By.cssSelector("#more-information p");
    private static final By ADD_TO_CART_BTN = By.xpath("//a[contains(@class,'btn-success') and contains(text(),'Add to cart')]");

    private final BrowserAlertHandler alertHandler;

    public ProductPage(WebDriver driver) {
        super(driver);
        this.alertHandler = new BrowserAlertHandler(driver);
    }

    public String getProductName() {
        return getText(PRODUCT_NAME);
    }

    public String getPriceText() {
        return getText(PRODUCT_PRICE);
    }

    public double getNumericPrice() {
        String raw = getPriceText().replaceAll("[^0-9.]", "");
        if (raw.isBlank()) {
            throw new NumberFormatException("Cannot parse price from: " + getPriceText());
        }
        return Double.parseDouble(raw);
    }

    public String getProductDescription() {
        return getText(PRODUCT_DESC);
    }

    public boolean isAddToCartButtonDisplayed() {
        return isDisplayed(ADD_TO_CART_BTN);
    }

    /**
     * Clicks "Add to cart" and accepts the resulting browser alert.
     * DemoBlaze displays window.alert("Product added") on success.
     *
     * @return alert text
     */
    public String addCurrentProductToCart() {
        log.info("Adding current product to cart.");
        click(ADD_TO_CART_BTN);
        String alertText = alertHandler.acceptAlert();
        log.info("Add to cart alert: '{}'", alertText);
        return alertText;
    }

    public static String expectedAddToCartMessage() {
        return "Product added";
    }
}
