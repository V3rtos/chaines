package me.moonways.rsap.api.transport;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.intellij.lang.annotations.MagicConstant;

import java.util.Collections;
import java.util.List;

@Builder
@ToString
public class RemoteCallback {

    @Getter(onMethod_ =
    @MagicConstant(valuesFromClass = StatusTypes.class))
    private final byte status;

    @Getter(onMethod_ =
    @MagicConstant(valuesFromClass = StatusCodes.class))
    private final byte statusCode;

    private final List<Object> returnValues;

    @Getter(onMethod_ =
    @MagicConstant(valuesFromClass = ReturnTypes.class))
    private final byte returnType;

    public List<Object> getReturnValues() {
        return (status == StatusTypes.FAILED ? Collections.emptyList() : Collections.unmodifiableList(returnValues));
    }

    public interface StatusTypes {
        byte SUCCESS = 0;
        byte FAILED = -1;
    }

    public interface StatusCodes {
        byte SUCCESSFUL = 0;
        byte ERR_ALREADY_EXPORTED = 1;
        byte ERR_NO_SIGNATURE = 3;
        byte ERR_INTERNAL = 3;
    }

    public interface ReturnTypes {
        byte METHOD_RETURN_VALUE = 10;
        byte METHOD_ARGUMENTS = 20;
    }
}
