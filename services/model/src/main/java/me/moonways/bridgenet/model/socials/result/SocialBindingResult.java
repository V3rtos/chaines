package me.moonways.bridgenet.model.socials.result;

public enum SocialBindingResult {

    SUCCESS,
    FAILURE__NO_CONNECTION,
    FAILURE__SOCIAL_NOT_EXISTS,
    FAILURE__PROFILE_NOT_FOUND,
    FAILURE__NOT_LINKED,
    FAILURE__NOT_BELONG,
    FAILURE__ALREADY_LINKED,
    FAILURE__UNCORRECTED_INPUT,
}
