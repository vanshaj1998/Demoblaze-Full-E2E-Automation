package com.example.demoblaze.pages;

import com.example.demoblaze.pages.components.PlaceOrderModal;
import com.example.demoblaze.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the DemoBlaze shopping cart page (cart.html).
 */
public class CartPage extends BasePage {

    private static final By CART_TABLE_ROWS = By.cssSelector("#tbodyid tr");
    private static final By PRODUCT_TITLES  = By.cssSelector("#tbodyid tr td:nth-child(2)");
    private static final By PRODUCT_PRICES  = By.cssSelector("#tbodyid tr td:nth-child(3)");
    private static final By TOTAL_PRICE     = By.id("totalp");
    private static final By PLACE_ORDER_BTN = By.xpath("//button[@data-target='#orderModal']");

    private final PlaceOrderModal placeOrderModal;

    public CartPage(WebDriver driver) {
        super(driver);
        this.placeOrderModal = new PlaceOrderModal(driver);
    }

    public CartPage open() {
        String cartUrl = "https://www.demoblaze.com/cart.html";
        log.info("Opening cart page: {}", cartUrl);
        navigateTo(cartUrl);
        return this;
    }

    public List<String> getCartProductNames() {
        waitForCartToLoad();
        return driver.findElements(PRODUCT_TITLES).stream()
                .map(e -> e.getText().trim())
                .filter(t -> !t.isBlank())
                .toList();
    }

    public int getCartItemCount() {
        waitForCartToLoad();
        return driver.findElements(CART_TABLE_ROWS).size();
    }

    public boolean waitForCartItemsCount(int minCount, int timeoutSeconds) {
        return WaitUtils.waitForElementsPresent(driver, CART_TABLE_ROWS, minCount, timeoutSeconds);
    }

    public String getTotalPriceText() {
        try {
            return getText(TOTAL_PRICE);
        } catch (Exception e) {
            log.warn("Total price element not yet populated.");
            return "0";
        }
    }

    public double getCartTotal() {
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                    .until(d -> {
                        String t = d.findElement(TOTAL_PRICE).getText().trim().replaceAll("[^0-9.]", "");
                        return !t.isBlank() && Double.parseDouble(t) > 0;
                    });
        } catch (Exception ignored) {}
        String raw = getTotalPriceText().replaceAll("[^0-9.]", "");
        if (raw.isBlank()) return 0.0;
        return Double.parseDouble(raw);
    }

    public List<Double> getProductPrices() {
        return driver.findElements(PRODUCT_PRICES).stream()
                .map(e -> {
                    String text = e.getText().trim().replaceAll("[^0-9.]", "");
                    return text.isBlank() ? 0.0 : Double.parseDouble(text);
                })
                .toList();
    }

    public boolean isProductInCart(String productName) {
        return getCartProductNames().stream()
                .anyMatch(name -> name.equalsIgnoreCase(productName));
    }

    public void removeProductFromCart(String productName) {
        log.info("Removing product '{}' from cart.", productName);
        List<WebElement> rows = driver.findElements(CART_TABLE_ROWS);
        for (WebElement row : rows) {
            List<WebElement> titleCells = row.findElements(By.cssSelector("td:nth-child(2)"));
            if (!titleCells.isEmpty()
                    && titleCells.get(0).getText().trim().equalsIgnoreCase(productName)) {
                List<WebElement> deleteLinks = row.findElements(By.cssSelector("td:nth-child(4) a"));
                if (!deleteLinks.isEmpty()) {
                    deleteLinks.get(0).click();
                    log.info("Delete clicked for '{}'.", productName);
                    waitForInvisibility(By.xpath("//td[normalize-space()='" + productName + "']"));
                    return;
                }
            }
        }
        log.warn("Product '{}' was not found in the cart to remove.", productName);
    }

    public PlaceOrderModal clickPlaceOrder() {
        log.info("Clicking Place Order button.");
        click(PLACE_ORDER_BTN);
        return placeOrderModal;
    }

    private void waitForCartToLoad() {
        try {
            WaitUtils.waitForElementsPresent(driver, CART_TABLE_ROWS, 1, 10);
        } catch (Exception ignored) {
            // Cart may be empty
        }
    }
}
