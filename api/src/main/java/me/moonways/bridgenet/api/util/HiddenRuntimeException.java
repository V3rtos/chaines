package me.moonways.bridgenet.api.util;

import lombok.experimental.StandardException;

import java.io.PrintStream;
import java.io.PrintWriter;

@StandardException
public final class HiddenRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 5401393768340876631L;

    @Override
    public void printStackTrace() {
        // do nothing.
    }

    @Override
    public void printStackTrace(PrintStream s) {
        // do nothing.
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        // do nothing.
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return new StackTraceElement[0];
    }
}
