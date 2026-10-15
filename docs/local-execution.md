# Local Execution Guide – DemoBlaze E2E Automation

This guide provides reproducible commands to compile, configure, and execute the automated test suite on a local workstation.

---

## 1. Prerequisites

- **Java Development Kit (JDK):** Version 21 (Eclipse Temurin, OpenJDK, or Oracle).
  - Verify with: `java -version`
- **Apache Maven:** Version 3.9+ (or use included `./mvnw` / `mvnw.cmd`).
  - Verify with: `mvn -version`
- **Browsers:**
  - Google Chrome (latest version). Selenium Manager automatically downloads the matching ChromeDriver.
  - Mozilla Firefox (optional, for Firefox testing).
- **Allure Command-Line (Optional, for local report viewing):**
  - Install via Scoop (`scoop install allure`), Homebrew (`brew install allure`), or npm (`npm install -g allure-commandline`).

---

## 2. Baseline Commands

### Compile and Verify Project
```bash
./mvnw clean test-compile
```

### Run All Tests (Default TestNG Suite)
```bash
./mvnw clean test
```

---

## 3. Targeted Test Execution

### A. Run Smoke Tests Only
```bash
./mvnw clean test -Dgroups=smoke
```

### B. Run Regression Tests Only
```bash
./mvnw clean test -Dgroups=regression
```

### C. Run End-to-End Checkout Flow Only
```bash
./mvnw clean test -Dgroups=e2e
```

### D. Run a Single Test Class
```bash
./mvnw test -Dtest=PurchaseFlowTest
```

### E. Run a Single Test Method
```bash
./mvnw test -Dtest=PurchaseFlowTest#testCompleteEndToEndPurchaseFlow
```

---

## 4. Browser & Mode Customization

### Headless Execution (Recommended for CI / Background)
```bash
./mvnw clean test -Dheadless=true
```

### Cross-Browser Testing: Run on Firefox
```bash
./mvnw clean test -Dbrowser=firefox -Dheadless=true
```

### Pointing to Staging or Custom URL
```bash
./mvnw clean test -Dbase.url=https://www.demoblaze.com/
```

### Remote Grid Execution
```bash
./mvnw clean test -Dremote=true -Dgrid.url=http://localhost:4444/wd/hub -Dheadless=true
```

---

## 5. Viewing Test Reports and Evidence

### Allure Interactive HTML Report
Generate and serve the Allure report locally:
```bash
allure serve target/allure-results
```

Or generate a static report:
```bash
allure generate target/allure-results -o target/allure-report --clean
allure open target/allure-report
```

### Surefire Reports
Standard XML and HTML test summaries are available under:
`target/surefire-reports/index.html`

### Failure Screenshots & Logs
- Screenshots: `target/screenshots/`
- Framework Logs: `target/logs/automation.log`
