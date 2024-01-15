package me.moonways.bridgenet.test.engine.unit.step;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.test.engine.TestBridgenetBootstrap;
import me.moonways.bridgenet.test.engine.impl.UnexceptionallyFailure;
import me.moonways.bridgenet.test.engine.unit.TestRunnableStep;
import me.moonways.bridgenet.test.engine.unit.TestUnit;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.lang.reflect.Method;

@Log4j2
public class TestInvocationStep implements TestRunnableStep {

    @Override
    public void process(TestBridgenetBootstrap bootstrap, TestUnit testUnit) throws Exception {
        testUnit.peekAnnotated(Test.class, (testFunc) -> invokeTestFunction(bootstrap, testUnit, testFunc));
    }

    private void invokeTestFunction(TestBridgenetBootstrap bootstrap, TestUnit testUnit, Method testFunc) {
        RunNotifier notifier = testUnit.getNotifier();

        Description description = Description.createTestDescription(testUnit.getSource().getClass(), testFunc.getName());
        notifier.fireTestStarted(description);

        try {
            testUnit.invoke(testFunc.getName());
            log.info("Test function has exit successful: §a{}()", testFunc.getName());
        }
        catch (Exception exception) {
            log.info("§4Test function has exit failed: §c{}", testFunc);

            bootstrap.throwException(exception.getCause());
            notifier.fireTestFailure(new UnexceptionallyFailure(description));
        }

        notifier.fireTestFinished(description);
    }
}
