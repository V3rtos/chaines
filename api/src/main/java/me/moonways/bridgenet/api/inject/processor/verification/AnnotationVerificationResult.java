package me.moonways.bridgenet.api.inject.processor.verification;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
public class AnnotationVerificationResult {

    public enum Code {

        SUCCESS,
        FAILURE,
        NOT_ENOUGH_DATA,
    }

    private final Code code;
    private final String errorMessage;

    public boolean isSuccess() {
        return code == Code.SUCCESS;
    }
}
