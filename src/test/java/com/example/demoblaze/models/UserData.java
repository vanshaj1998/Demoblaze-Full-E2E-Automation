package com.example.demoblaze.models;

/**
 * Immutable value object representing a user account.
 * Allows empty/blank values for negative testing scenarios.
 */
public record UserData(String username, String password) {}
