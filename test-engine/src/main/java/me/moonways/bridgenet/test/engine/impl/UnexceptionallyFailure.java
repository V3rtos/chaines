package me.moonways.bridgenet.test.engine.impl;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

public class UnexceptionallyFailure extends Failure {

    /**
     * Constructs a <code>Failure</code> with the given description and exception.
     *
     * @param description     a {@link Description} of the test that failed
     */
    public UnexceptionallyFailure(Description description) {
        super(description, new RuntimeException());
    }

    @Override
    public String getTrace() {
        return "";
    }

    @Override
    public String getTrimmedTrace() {
        return "";
    }

    @Override
    public String getMessage() {
        return "";
    }
}
