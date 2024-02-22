package me.moonways.bridgenet.api.modern_command.execution.verify;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class CommandVerifyResult {

    private final ResultType resultType;
    @Getter
    private final String message;

    public boolean isOk() {
        return resultType.equals(ResultType.OK);
    }

    public boolean isFail() {
        return !isOk();
    }

    private CommandVerifyResult(ResultType resultType, String message) {
        this.resultType = resultType;
        this.message = message;
    }

    public static CommandVerifyResult ok(@Nullable String message) {
        return new CommandVerifyResult(ResultType.OK, message);
    }

    public static CommandVerifyResult ok() {
        return new CommandVerifyResult(ResultType.OK, null);
    }

    public static CommandVerifyResult fail(String message) {
        return new CommandVerifyResult(ResultType.FAIL, message);
    }

    public static CommandVerifyResult fail() {
        return new CommandVerifyResult(ResultType.FAIL, null);
    }

    enum ResultType {

        OK,
        FAIL
    }
}
