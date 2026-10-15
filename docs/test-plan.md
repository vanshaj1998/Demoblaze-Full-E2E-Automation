# Master Test Plan – DemoBlaze E2E UI Automation

**Document Version:** 1.0  
**Application Under Test:** [DemoBlaze E-Commerce](https://www.demoblaze.com/)  
**Environment:** Public Shared Staging/Demo  

---

## 1. Objectives

1. Validate the core user journeys of the DemoBlaze e-commerce platform end-to-end:
   - Product browsing and category filtering.
   - User account registration and authentication (login/logout).
   - Shopping cart operations (adding items, verifying dynamic calculations, removing items).
   - Checkout flow and order placement via modal forms.
   - UI feedback, form validation, and alert message handling.
2. Provide a scalable, production-grade test automation framework adhering to the Page Object Model (POM) and clean architecture standards.
3. Enable cross-browser, containerized, and CI/CD automated test execution with Allure and JUnit diagnostic reporting.

---

## 2. In-Scope vs. Out-of-Scope

### In-Scope
- **Home & Navigation:** Brand header, navigation links, hero carousel, product catalog populating.
- **Category Browsing:** Filtering catalog by Phones, Laptops, Monitors.
- **Product Details:** Product title, price parsing, specification text, and "Add to cart" action.
- **User Authentication:** Synthetic account registration, duplicate account prevention, positive login, wrong password rejection, non-existent user handling, blank field validation, and session logout.
- **Cart Management:** Cart item addition, multi-item table persistence, line-item removal, and numeric total recalculation.
- **End-to-End Checkout:** Full user purchase journey, modal customer details form, SweetAlert purchase confirmation dialog parsing, and post-purchase page responsiveness.
- **Negative & Resilient Scenarios:** Form input validation, missing required checkout fields, and JavaScript alert interception.

### Out-of-Scope
- Backend database direct verification (no direct DB access exposed by DemoBlaze).
- Real payment gateway transactions (DemoBlaze uses simulated order confirmation).
- High-concurrency load and performance testing.
- Cross-session cookie/local storage persistence across physical devices.

---

## 3. Test Strategy & Architecture

- **Automation Approach:** Pure Java 21 + Selenium 4 + TestNG.
- **Design Pattern:** Layered Page Object Model (BasePage, Page Objects, and Reusable UI Components).
- **Driver Lifecycle:** Isolated `ThreadLocal<WebDriver>` instances managed centrally via `DriverManager` and `DriverFactory`. Zero static global drivers.
- **Synchronization:** 100% explicit waits (`WebDriverWait` and `ExpectedConditions`). Fixed `Thread.sleep()` is prohibited.
- **Data Strategy:** Dynamic synthetic test data generated via `TestDataFactory` and `TestIdGenerator` (timestamp + atomic counter) to avoid collision in public shared environments.
- **Reporting & Diagnostics:** SLF4J + Logback for structured logging; Allure TestNG integration capturing screenshots, URLs, and stack traces automatically on failure.

---

## 4. Test Environments & Browser Coverage

| Component | Specification |
|---|---|
| Primary Browser | Google Chrome (Latest / 130+) |
| Secondary Browser | Mozilla Firefox (Latest / 131+) |
| Local Execution | Headed or Headless via Selenium Manager |
| Remote Execution | Selenium Grid 4 Standalone Container (`selenium/standalone-chrome:4.18.1`) |
| Base URL | `https://www.demoblaze.com/` |

---

## 5. Entry & Exit Criteria

### Entry Criteria
- Target demo environment (`https://www.demoblaze.com/`) is accessible and responsive.
- JDK 21 and Maven 3.9+ or Docker runtime installed.
- Required test dependencies resolve successfully via Maven.

### Exit Criteria
- 100% of defined Smoke tests pass.
- At least 95% of defined Regression tests pass.
- Full E2E purchase journey test passes deterministically.
- All failures generate actionable diagnostic evidence (screenshots, logs, and stack traces).
- Zero critical open framework bugs.

---

## 6. Risks & Mitigations

| Risk | Impact | Mitigation |
|---|---|---|
| **Public Shared State:** Other users or automated bots clearing or polluting shared data. | High | Always generate unique synthetic usernames (`qa_<hex>`) per test run; never assume state from prior tests. |
| **Demo App Slowness / Flakiness:** Dynamic catalog loading relies on asynchronous API calls. | Medium | Use explicit conditions (`waitForElementsPresent`, `waitForVisibility`) with configurable timeouts (default 15s). |
| **Browser Native Alerts:** DemoBlaze relies on native `window.alert()` rather than DOM modals. | Medium | Centralized `BrowserAlertHandler` dedicated to safely waiting, reading, and accepting native alerts. |
| **Docker Resource Starvation:** Chrome renderer crashes in Docker containers. | Medium | Set `shm_size: 2gb` in `docker-compose.yml` to prevent `/dev/shm` buffer exhaustion. |
