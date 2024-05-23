package me.moonways.bridgenet.model.service.auth;

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
    // Change Password
    FAILURE__SIMILAR_PREVIOUS_PASSWORDS,

    // Log Out
    FAILURE__NOT_LOGGED,

    // Account Delete / Change Password / Login
    FAILURE__ACCOUNT_NOT_FOUND
}
