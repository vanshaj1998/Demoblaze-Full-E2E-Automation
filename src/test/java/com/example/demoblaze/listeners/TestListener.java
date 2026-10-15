package com.example.demoblaze.listeners;

import com.example.demoblaze.driver.DriverManager;
import com.example.demoblaze.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for execution lifecycle logging and automated failure diagnostics.
 * Captures screenshots, records current URL, and attaches diagnostics to Allure.
 */
public class TestListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("========== SUITE START: {} ==========", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("========== SUITE FINISH: {} (Passed: {}, Failed: {}, Skipped: {}) ==========",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testId = extractTestId(result);
        log.info(">>> START TEST [{}] {}.{}()",
                testId,
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("<<< PASSED: {}.{}() [{}ms]",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName(),
                result.getEndMillis() - result.getStartMillis());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String className  = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();
        String testName   = className + "_" + methodName;
        Throwable throwable = result.getThrowable();

        log.error("<<< FAILED: {}.{}() - Cause: {}", className, methodName,
                throwable != null ? throwable.getMessage() : "Unknown");

        if (DriverManager.hasDriver()) {
            try {
                WebDriver driver = DriverManager.getDriver();
                String currentUrl = driver.getCurrentUrl();
                log.info("Failure URL: {}", currentUrl);
                Allure.addAttachment("Failure URL", "text/plain", currentUrl);

                // Capture and attach screenshot
                ScreenshotUtils.captureAndAttach(driver, testName);
            } catch (Exception e) {
                log.warn("Could not capture diagnostic failure state: {}", e.getMessage());
            }
        }

        if (throwable != null) {
            Allure.addAttachment("Stacktrace", "text/plain",
                    getStackTraceAsString(throwable));
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("<<< SKIPPED: {}.{}()",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }

    private String extractTestId(ITestResult result) {
        String desc = result.getMethod().getDescription();
        if (desc != null && desc.startsWith("DB-")) {
            return desc.split(" ")[0];
        }
        return "N/A";
    }

    private String getStackTraceAsString(Throwable t) {
        java.io.StringWriter sw = new java.io.StringWriter();
        t.printStackTrace(new java.io.PrintWriter(sw));
        return sw.toString();
    }
}
