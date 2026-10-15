package com.example.demoblaze.pages.components;

import com.example.demoblaze.models.CustomerData;
import com.example.demoblaze.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Modal component for checkout (Place Order popup #orderModal)
 * and its SweetAlert purchase confirmation dialog.
 */
public class PlaceOrderModal extends BasePage {

    private static final By MODAL_CONTAINER      = By.id("orderModal");
    private static final By TOTAL_LABEL          = By.id("totalm");
    private static final By NAME_INPUT           = By.id("name");
    private static final By COUNTRY_INPUT        = By.id("country");
    private static final By CITY_INPUT           = By.id("city");
    private static final By CARD_INPUT           = By.id("card");
    private static final By MONTH_INPUT          = By.id("month");
    private static final By YEAR_INPUT           = By.id("year");
    private static final By PURCHASE_BUTTON      = By.xpath("//button[@onclick='purchaseOrder()']");
    private static final By CLOSE_BUTTON         = By.cssSelector("#orderModal .btn-secondary");

    // SweetAlert confirmation elements
    private static final By SWEET_ALERT_DIALOG   = By.cssSelector(".sweet-alert");
    private static final By CONFIRMATION_HEADING = By.cssSelector(".sweet-alert h2");
    private static final By CONFIRMATION_DETAILS = By.cssSelector(".sweet-alert p.lead");
    private static final By CONFIRM_OK_BUTTON    = By.cssSelector(".sweet-alert button.confirm");

    private final BrowserAlertHandler alertHandler;

    public PlaceOrderModal(WebDriver driver) {
        super(driver);
        this.alertHandler = new BrowserAlertHandler(driver);
    }

    public boolean isDisplayed() {
        return isDisplayed(MODAL_CONTAINER);
    }

    public String getModalTotalText() {
        return getText(TOTAL_LABEL);
    }

    public PlaceOrderModal fillCustomerForm(CustomerData customer) {
        log.info("Filling Place Order form for customer: '{}'", customer.name());
        type(NAME_INPUT, customer.name());
        type(COUNTRY_INPUT, customer.country());
        type(CITY_INPUT, customer.city());
        type(CARD_INPUT, customer.creditCard());
        type(MONTH_INPUT, customer.month());
        type(YEAR_INPUT, customer.year());
        return this;
    }

    public void clickPurchase() {
        log.info("Clicking Purchase button in order modal.");
        click(PURCHASE_BUTTON);
    }

    /**
     * Submits customer details and waits for the SweetAlert confirmation dialog.
     */
    public void submitOrder(CustomerData customer) {
        fillCustomerForm(customer);
        clickPurchase();
        findVisible(SWEET_ALERT_DIALOG);
    }

    /**
     * Submits order expecting an alert (e.g. "Please fill out Name and Creditcard.")
     */
    public String submitOrderExpectingAlert(CustomerData customer) {
        fillCustomerForm(customer);
        clickPurchase();
        return alertHandler.acceptAlert();
    }

    public boolean isConfirmationDisplayed() {
        return isDisplayed(SWEET_ALERT_DIALOG);
    }

    public String getConfirmationHeading() {
        return getText(CONFIRMATION_HEADING);
    }

    public String getConfirmationDetails() {
        return getText(CONFIRMATION_DETAILS);
    }

    public void confirmPurchaseAndClose() {
        log.info("Confirming purchase dialog.");
        click(CONFIRM_OK_BUTTON);
        waitForInvisibility(SWEET_ALERT_DIALOG);
    }

    public void close() {
        click(CLOSE_BUTTON);
        waitForInvisibility(MODAL_CONTAINER);
    }
}
