package com.example.demoblaze.data;

import com.example.demoblaze.models.CustomerData;
import com.example.demoblaze.models.UserData;
import com.example.demoblaze.utils.TestIdGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Centralised factory for all test data.
 *
 * <p>Usernames are generated with a unique suffix so that registration
 * tests do not collide in the shared DemoBlaze environment across parallel runs
 * or repeated executions.
 */
public final class TestDataFactory {

    private static final Logger log = LoggerFactory.getLogger(TestDataFactory.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String DEFAULT_PASSWORD = "Test@Password123!";

    // Shared existing user credentials (for login tests)
    public static final String EXISTING_USERNAME = "demoblaze_qa_user";
    public static final String EXISTING_PASSWORD = "Test@Password123!";

    private TestDataFactory() { /* utility class */ }

    // -------------------------------------------------------------------------
    // User accounts
    // -------------------------------------------------------------------------

    public static UserData uniqueUser() {
        String username = "qa_" + TestIdGenerator.uniqueSuffix();
        log.debug("Generated unique test user: {}", username);
        return new UserData(username, DEFAULT_PASSWORD);
    }

    public static UserData existingUser() {
        return new UserData(EXISTING_USERNAME, EXISTING_PASSWORD);
    }

    public static UserData userWithWrongPassword() {
        return new UserData(EXISTING_USERNAME, "WrongPass999_xyz");
    }

    public static UserData userWithBlankUsername() {
        return new UserData("", DEFAULT_PASSWORD);
    }

    public static UserData userWithBlankPassword() {
        return new UserData("qa_user_" + TestIdGenerator.uniqueSuffix(), "");
    }

    public static UserData userWithSpecialChars() {
        return new UserData("qa_<script>" + TestIdGenerator.uniqueSuffix(), DEFAULT_PASSWORD);
    }

    public static UserData userWithLongUsername() {
        String longName = "qa_" + "a".repeat(200) + "_" + TestIdGenerator.uniqueSuffix();
        return new UserData(longName, DEFAULT_PASSWORD);
    }

    // -------------------------------------------------------------------------
    // Customer / checkout data (all synthetic)
    // -------------------------------------------------------------------------

    public static CustomerData validCustomer() {
        return new CustomerData(
                "Synthetic QA Buyer",
                "United States",
                "Austin",
                "4111111111111111",   // Standard test card format
                "12",
                "2026"
        );
    }

    public static CustomerData customerWithBlankRequiredFields() {
        return new CustomerData(
                "",           // missing Name
                "United States",
                "Austin",
                "",           // missing Credit Card
                "12",
                "2026"
        );
    }

    public static CustomerData customerWithOnlyName() {
        return new CustomerData(
                "QA Buyer",
                "",
                "",
                "",
                "",
                ""
        );
    }

    public static CustomerData customerWithOnlyCard() {
        return new CustomerData(
                "",
                "United States",
                "Austin",
                "4111111111111111",
                "12",
                "2026"
        );
    }

    public static CustomerData customerPartialOptionalOnly() {
        return new CustomerData(
                "QA Buyer Partial",
                "",
                "",
                "4111111111111111",
                "",
                ""
        );
    }

    // -------------------------------------------------------------------------
    // Product data from JSON
    // -------------------------------------------------------------------------

    public static List<String[]> loadProductData() {
        List<String[]> result = new ArrayList<>();
        try (InputStream is = TestDataFactory.class
                .getClassLoader().getResourceAsStream("testdata/products.json")) {
            if (is == null) {
                log.warn("testdata/products.json not found on classpath.");
                return result;
            }
            JsonNode root = MAPPER.readTree(is);
            if (root.isArray()) {
                for (JsonNode node : root) {
                    String name     = node.path("name").asText();
                    String category = node.path("category").asText();
                    String price    = node.path("price").asText();
                    result.add(new String[]{name, category, price});
                }
            }
        } catch (IOException e) {
            log.error("Failed to load products.json: {}", e.getMessage());
        }
        return result;
    }
}
