package me.moonways.bridgenet.rest.server.controller.verify;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class VerificationConfig {

    private static final int CREDENTIALS_VERIFIED_TYPE = 2;

    @Setter
    private boolean isPublic, isVerified;

    private int credentialsVerifyType;

    public boolean isCredentialsVerified() {
        return credentialsVerifyType == CREDENTIALS_VERIFIED_TYPE;
    }

    public void increaseCredentialsVerify() {
        ++credentialsVerifyType;
    }
}
