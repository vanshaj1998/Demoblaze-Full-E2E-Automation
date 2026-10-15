package com.example.demoblaze.models;

/**
 * Immutable value object representing checkout customer details.
 * Use only synthetic test data – never real personal information.
 * Allows blank values for testing checkout validation.
 */
public record CustomerData(
        String name,
        String country,
        String city,
        String creditCard,
        String month,
        String year
) {}
