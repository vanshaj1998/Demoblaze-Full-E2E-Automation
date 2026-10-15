package com.example.demoblaze.utils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Generates unique identifiers for synthetic test data to avoid collisions
 * in the shared DemoBlaze demo environment.
 */
public final class TestIdGenerator {

    private static final AtomicLong COUNTER = new AtomicLong(System.currentTimeMillis());

    private TestIdGenerator() {}

    /**
     * Returns a short alphanumeric suffix composed of a timestamp and counter.
     */
    public static String uniqueSuffix() {
        return Long.toHexString(COUNTER.getAndIncrement());
    }

    /** Returns a full UUID string for higher entropy requirements. */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
