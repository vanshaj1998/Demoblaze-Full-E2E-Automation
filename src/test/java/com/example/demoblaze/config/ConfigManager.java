package com.example.demoblaze.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Central configuration manager.
 *
 * <p>Configuration precedence (highest to lowest):
 * <ol>
 *   <li>JVM system properties (-Dproperty=value on the CLI or from Surefire)</li>
 *   <li>Environment variables (BROWSER, HEADLESS, REMOTE, GRID_URL, etc.)</li>
 *   <li>config.properties on the classpath</li>
 *   <li>Hard-coded safe defaults</li>
 * </ol>
 *
 * <p>Never commit secrets. Use environment variables or CI secrets for credentials.
 */
public final class ConfigManager {

    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
    private static final String CONFIG_FILE = "config.properties";

    private static final ConfigManager INSTANCE = new ConfigManager();

    private final Properties fileProps = new Properties();

    private ConfigManager() {
        loadPropertiesFile();
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    // -------------------------------------------------------------------------
    // Public getters
    // -------------------------------------------------------------------------

    public String getBaseUrl() {
        return get("base.url", "https://www.demoblaze.com/");
    }

    public String getBrowser() {
        return get("browser", "chrome").toLowerCase();
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(get("headless", "false"));
    }

    public boolean isRemote() {
        return Boolean.parseBoolean(get("remote", "false"));
    }

    /** Returns the Selenium Grid URL if configured, or default empty string for local. */
    public String getGridUrl() {
        return get("grid.url", "");
    }

    public int getExplicitWaitSeconds() {
        return Integer.parseInt(get("explicit.wait.seconds", "15"));
    }

    public int getPageLoadTimeoutSeconds() {
        return Integer.parseInt(get("page.load.timeout.seconds", "30"));
    }

    public String getEnvironment() {
        return get("env", "test");
    }

    public String getTestngSuite() {
        return get("testng.suite", "src/test/resources/testng.xml");
    }

    // -------------------------------------------------------------------------
    // Internal resolution
    // -------------------------------------------------------------------------

    public String get(String key, String defaultValue) {
        // 1. JVM system property
        String value = System.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value.trim();
        }

        // 2. Environment variable (e.g. base.url -> BASE_URL)
        String envKey = key.toUpperCase().replace('.', '_');
        value = System.getenv(envKey);
        if (value != null && !value.isBlank()) {
            return value.trim();
        }

        // 3. config.properties
        value = fileProps.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value.trim();
        }

        // 4. Default
        return defaultValue;
    }

    public String getRequired(String key) {
        String value = get(key, null);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required configuration property '" + key + "' is missing. "
                    + "Set it via -D" + key + "=value, the environment variable "
                    + key.toUpperCase().replace('.', '_') + ", or in config.properties.");
        }
        return value;
    }

    private void loadPropertiesFile() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is == null) {
                log.warn("'{}' not found on classpath – using system properties and env vars only.", CONFIG_FILE);
                return;
            }
            fileProps.load(is);
            log.info("Loaded configuration from '{}'.", CONFIG_FILE);
        } catch (IOException e) {
            log.error("Failed to load '{}': {}", CONFIG_FILE, e.getMessage());
        }
    }
}
