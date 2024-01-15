package me.moonways.bridgenet.test.engine.unit.step;

import me.moonways.bridgenet.test.engine.BridgenetBootstrapInitializer;
import me.moonways.bridgenet.test.engine.unit.TestRunnableStep;
import me.moonways.bridgenet.test.engine.unit.TestUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.lang.reflect.Method;

public class TestInvocationStep implements TestRunnableStep {

    @Override
    public void process(BridgenetBootstrapInitializer initializer, TestUnit testUnit) throws Exception {
        testUnit.peekAnnotated(Test.class, (testFunc) -> invokeTestFunction(initializer, testUnit, testFunc));
    }

    private void invokeTestFunction(BridgenetBootstrapInitializer initializer, TestUnit testUnit, Method testFunc) {
        RunNotifier notifier = testUnit.getNotifier();

        Description description = Description.createTestDescription(testUnit.getSource().getClass(), testFunc.getName());
        notifier.fireTestStarted(description);

        try {
            testUnit.invoke(testFunc.getName());
        }
        catch (Exception exception) {
            initializer.throwException(exception.getCause());
            notifier.fireTestIgnored(description);
        }

        notifier.fireTestFinished(description);
    }
}
