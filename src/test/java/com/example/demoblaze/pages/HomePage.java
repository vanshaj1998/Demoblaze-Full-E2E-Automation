package com.example.demoblaze.pages;

import com.example.demoblaze.config.ConfigManager;
import com.example.demoblaze.pages.components.NavigationBar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the DemoBlaze home page (index.html).
 * Covers the product catalogue, category selection, and carousel area.
 */
public class HomePage extends BasePage {

    private static final By CAROUSEL           = By.id("carouselExampleIndicators");
    private static final By CAROUSEL_WRAPPER   = By.id("contcar");
    private static final By CATEGORY_ITEMS     = By.cssSelector("#contcont .list-group-item");
    private static final By PRODUCT_CARDS      = By.cssSelector("#tbodyid .card");
    private static final By PRODUCT_CARD_LINKS = By.cssSelector("#tbodyid .card .card-title a");
    private static final By CATEGORY_PHONE     = By.xpath("//a[contains(@onclick,\"byCat('phone')\")]");
    private static final By CATEGORY_LAPTOP    = By.xpath("//a[contains(@onclick,\"byCat('notebook')\")]");
    private static final By CATEGORY_MONITOR   = By.xpath("//a[contains(@onclick,\"byCat('monitor')\")]");
    private static final By NEXT_BUTTON        = By.id("next2");
    private static final By PREV_BUTTON        = By.id("prev2");
    private static final By PRODUCT_CARD_PRICES = By.cssSelector("#tbodyid .card h5");
    private static final By PRODUCT_CARD_IMGS  = By.cssSelector("#tbodyid .card img");
    private static final By FOOTER_SECTION     = By.id("footc");
    private static final By CONTACT_MODAL      = By.id("exampleModal");
    private static final By ABOUT_MODAL        = By.id("videoModal");

    private final NavigationBar navBar;

    public HomePage(WebDriver driver) {
        super(driver);
        this.navBar = new NavigationBar(driver);
    }

    public HomePage open() {
        String url = ConfigManager.getInstance().getBaseUrl();
        log.info("Opening home page: {}", url);
        navigateTo(url);
        waitForProductsToLoad(PRODUCT_CARDS);
        return this;
    }

    public NavigationBar getNavigationBar() {
        return navBar;
    }

    public HomePage openCategory(String category) {
        log.info("Selecting category: {}", category);
        By locator = switch (category.toLowerCase()) {
            case "phone", "phones"       -> CATEGORY_PHONE;
            case "laptop", "laptops"     -> CATEGORY_LAPTOP;
            case "monitor", "monitors"   -> CATEGORY_MONITOR;
            default -> throw new IllegalArgumentException("Unknown category: " + category);
        };
        click(locator);
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        waitForProductsToLoad(PRODUCT_CARDS);
        return this;
    }

    public void waitForProducts() {
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        waitForProductsToLoad(PRODUCT_CARDS);
    }

    public ProductPage openFirstProduct() {
        List<WebElement> links = findAllVisible(PRODUCT_CARD_LINKS);
        if (links.isEmpty()) {
            throw new IllegalStateException("No product links visible on the home page.");
        }
        String productName = links.get(0).getText();
        log.info("Opening first product: '{}'", productName);
        click(links.get(0));
        return new ProductPage(driver);
    }

    public ProductPage openProduct(String productName) {
        log.info("Opening product: '{}'", productName);
        List<WebElement> links = findAllVisible(PRODUCT_CARD_LINKS);
        for (WebElement link : links) {
            if (link.getText().trim().equalsIgnoreCase(productName)) {
                click(link);
                return new ProductPage(driver);
            }
        }
        throw new IllegalStateException("Product not found in current list: " + productName);
    }

    public List<String> getDisplayedProductNames() {
        return findAllVisible(PRODUCT_CARD_LINKS).stream()
                .map(e -> e.getText().trim())
                .toList();
    }

    public int getProductCount() {
        try {
            return driver.findElements(PRODUCT_CARDS).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isCarouselDisplayed() {
        try {
            return isDisplayed(CAROUSEL) || isDisplayed(CAROUSEL_WRAPPER)
                    || (!driver.findElements(CAROUSEL).isEmpty() && driver.findElement(CAROUSEL).isDisplayed());
        } catch (Exception e) {
            return !driver.findElements(CAROUSEL).isEmpty();
        }
    }

    public List<String> getAvailableCategories() {
        return findAllVisible(CATEGORY_ITEMS).stream()
                .map(e -> e.getText().trim())
                .filter(t -> !t.isBlank())
                .toList();
    }

    public void clickNextPage() {
        log.info("Clicking Next page button");
        click(NEXT_BUTTON);
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
    }

    public void clickPrevPage() {
        log.info("Clicking Previous page button");
        click(PREV_BUTTON);
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
    }

    public boolean isNextButtonDisplayed() {
        return isDisplayed(NEXT_BUTTON);
    }

    public boolean isPrevButtonDisplayed() {
        return isDisplayed(PREV_BUTTON);
    }

    public List<String> getDisplayedProductPrices() {
        return driver.findElements(PRODUCT_CARD_PRICES).stream()
                .map(e -> e.getText().trim())
                .filter(t -> !t.isBlank())
                .toList();
    }

    public boolean hasProductImages() {
        try {
            List<WebElement> imgs = driver.findElements(PRODUCT_CARD_IMGS);
            return !imgs.isEmpty() && imgs.stream().allMatch(img -> {
                String src = img.getAttribute("src");
                return src != null && !src.isBlank();
            });
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFooterDisplayed() {
        return isDisplayed(FOOTER_SECTION);
    }

    public boolean isContactModalDisplayed() {
        return isDisplayed(CONTACT_MODAL);
    }

    public boolean isAboutModalDisplayed() {
        return isDisplayed(ABOUT_MODAL);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
