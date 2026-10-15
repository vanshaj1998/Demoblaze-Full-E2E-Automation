package com.example.demoblaze.driver;

import com.example.demoblaze.config.ConfigManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

/**
 * Responsible for creating WebDriver instances.
 *
 * <p>Selenium Manager is used automatically by Selenium 4 for local execution.
 * For remote / Grid execution configure {@code remote=true} and {@code grid.url=http://...:4444/wd/hub}.
 */
public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() { /* utility class */ }

    public static WebDriver createDriver() {
        ConfigManager cfg = ConfigManager.getInstance();
        String browser = cfg.getBrowser();
        boolean remote = cfg.isRemote();

        log.info("Creating '{}' driver | remote={} | headless={}", browser, remote, cfg.isHeadless());

        WebDriver driver;
        if (remote) {
            driver = createRemoteDriver(cfg, browser);
        } else {
            driver = createLocalDriver(cfg, browser);
        }

        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(cfg.getPageLoadTimeoutSeconds()));
        driver.manage().timeouts().scriptTimeout(
                Duration.ofSeconds(cfg.getPageLoadTimeoutSeconds()));
        driver.manage().window().setSize(new Dimension(1920, 1080));

        log.info("Driver created successfully: {}", driver.getClass().getSimpleName());
        return driver;
    }

    private static WebDriver createLocalDriver(ConfigManager cfg, String browser) {
        return switch (browser) {
            case "firefox" -> new FirefoxDriver(buildFirefoxOptions(cfg));
            case "chrome"  -> new ChromeDriver(buildChromeOptions(cfg));
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: '" + browser + "'. Supported values: chrome, firefox.");
        };
    }

    private static WebDriver createRemoteDriver(ConfigManager cfg, String browser) {
        String gridUrl = cfg.getGridUrl();
        if (gridUrl == null || gridUrl.isBlank()) {
            throw new IllegalStateException(
                    "remote=true but grid.url is not configured. "
                    + "Set grid.url=http://<grid-host>:4444/wd/hub in config.properties "
                    + "or via the GRID_URL environment variable.");
        }

        try {
            return switch (browser) {
                case "firefox" -> new RemoteWebDriver(
                        URI.create(gridUrl).toURL(), buildFirefoxOptions(cfg));
                case "chrome"  -> new RemoteWebDriver(
                        URI.create(gridUrl).toURL(), buildChromeOptions(cfg));
                default -> throw new IllegalArgumentException(
                        "Unsupported browser for remote execution: '" + browser + "'.");
            };
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid grid.url: " + gridUrl, e);
        }
    }

    private static ChromeOptions buildChromeOptions(ConfigManager cfg) {
        ChromeOptions options = new ChromeOptions();
        if (cfg.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080",
                "--disable-extensions",
                "--remote-allow-origins=*"
        );
        return options;
    }

    private static FirefoxOptions buildFirefoxOptions(ConfigManager cfg) {
        FirefoxOptions options = new FirefoxOptions();
        if (cfg.isHeadless()) {
            options.addArguments("-headless");
        }
        options.addArguments("--width=1920", "--height=1080");
        return options;
    }
}
