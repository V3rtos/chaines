package me.moonways.bridgenet.model.auth;

public enum AuthorizationResult {

    // Global
    SUCCESS,
    FAILURE,

    // Registration
    FAILURE__ALREADY_REGISTERED,

    // Login.
    FAILURE__ALREADY_LOGGED,
    // Login / Change Password
    FAILURE__UNCORRECTED_PASSWORD,

    // Log Out
    FAILURE__NOT_LOGGED,

    // Account Delete / Change Password / Login
    FAILURE__ACCOUNT_NOT_FOUND
}
