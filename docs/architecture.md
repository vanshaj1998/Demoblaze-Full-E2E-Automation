# Framework Architecture – DemoBlaze E2E Automation

The DemoBlaze E2E Automation Framework is built on clean architecture principles, strict separation of concerns, and enterprise test engineering standards.

---

## 1. Layered Framework Design

```
+------------------------------------------------------------------------+
|                          Test Execution Layer                          |
|         (TestNG Suites, Tests: Smoke, Regression, End-to-End)         |
+------------------------------------------------------------------------+
                                   |
         +-------------------------+-------------------------+
         |                                                   |
         v                                                   v
+-----------------------------+             +----------------------------+
|     Assertions & Models     |             |     Page Object Layer      |
|  - CartAssertions           |             |  - HomePage, ProductPage   |
|  - UserData, CustomerData   |             |  - CartPage                |
+-----------------------------+             +----------------------------+
                                                             |
                                                             v
                                            +----------------------------+
                                            |   Reusable UI Components   |
                                            |  - NavigationBar           |
                                            |  - LoginModal, SignupModal |
                                            |  - PlaceOrderModal         |
                                            |  - BrowserAlertHandler     |
                                            +----------------------------+
                                                             |
                                                             v
                                            +----------------------------+
                                            |          BasePage          |
                                            |  Explicit Waits & Actions  |
                                            +----------------------------+
                                                             |
         +---------------------------------------------------+
         v
+-----------------------------+             +----------------------------+
|   Driver Management Layer   |             |    Configuration Layer     |
|  - DriverFactory (Options)  | <---------- |  - ConfigManager           |
|  - DriverManager (ThreadLoc)|             |  - JVM -> Env -> Properties|
+-----------------------------+             +----------------------------+
         |
         v
+-----------------------------+
|    Diagnostic & Logging     |
|  - TestListener (Allure)    |
|  - ScreenshotUtils          |
|  - SLF4J / Logback          |
+-----------------------------+
```

---

## 2. Core Architectural Principles

### A. Separation of Page Mechanics and Test Assertions
- **Page Objects** encapsulate HTML locators, page state transitions, and business action methods (`openCategory`, `loginAs`, `addCurrentProductToCart`, `completePurchase`).
- Page Objects **never** contain TestNG `Assert` statements.
- **Test Classes** orchestrate user workflows, invoking Page Objects and verifying business outcomes through explicit assertion helpers (`CartAssertions`, TestNG `Assert`).

### B. Safe Driver Lifecycle Management (No Static Drivers)
- Uses `ThreadLocal<WebDriver>` within `DriverManager` to guarantee test isolation.
- `BaseTest` manages driver initialization (`@BeforeMethod`) and teardown (`@AfterMethod`), ensuring browser processes are always cleaned up even upon unexpected test failures.
- No test method or page object directly initializes or quits the WebDriver.

### C. Deterministic Synchronization (No Thread.sleep)
- All UI interactions rely strictly on `WaitUtils` and `WebDriverWait` utilizing explicit Expected Conditions (`visibilityOfElementLocated`, `elementToBeClickable`, `invisibilityOfElementLocated`).
- Zero arbitrary `Thread.sleep()` calls exist in the framework.

### D. Layered Configuration Precedence
`ConfigManager` evaluates settings using strict priority ordering:
1. **JVM System Properties** (`-Dbase.url=...`, `-Dbrowser=...`, `-Dheadless=true`)
2. **Environment Variables** (`BASE_URL`, `BROWSER`, `HEADLESS`, `GRID_URL`)
3. **ClassPath Property File** (`config.properties`)
4. **Sensible Fallback Defaults** (`https://www.demoblaze.com/`, `chrome`, `false`)

### E. Diagnostic Observability & Reporting
- Test execution lifecycle is intercepted via `TestListener`.
- When any test fails, `ScreenshotUtils` captures a full-screen image, writes it to `target/screenshots/`, and automatically attaches it alongside the current URL and stack trace to the Allure report.
- Structured logging with Logback writes human-readable logs to the console and detailed timestamped records to `target/logs/automation.log`.
