package me.moonways.bridgenet.test.engine.unit.step;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.test.engine.TestBridgenetBootstrap;
import me.moonways.bridgenet.test.engine.junit.UnexceptionallyFailure;
import me.moonways.bridgenet.test.engine.persistance.SleepExecution;
import me.moonways.bridgenet.test.engine.unit.TestRunnableStep;
import me.moonways.bridgenet.test.engine.unit.TestClassUnit;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import java.lang.reflect.Method;

@Log4j2
public class TestInvocationStep implements TestRunnableStep {

    @Override
    public void process(TestBridgenetBootstrap bootstrap, TestClassUnit testUnit) throws Exception {
        testUnit.peekAnnotated(Test.class, (testFunc) -> invokeTestFunction(bootstrap, testUnit, testFunc));
    }

    private void invokeTestFunction(TestBridgenetBootstrap bootstrap, TestClassUnit testUnit, Method testFunc) {
        RunNotifier notifier = testUnit.getNotifier();

        if (testFunc.isAnnotationPresent(SleepExecution.class)) {
            processSleeping(testFunc);
        }

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

    @SneakyThrows
    private void processSleeping(Method method) {
        SleepExecution sleepExecution = method.getDeclaredAnnotation(SleepExecution.class);

        long millis = sleepExecution.unit().toMillis(sleepExecution.duration());
        log.debug("§cSleeping test-units execution on {} ms by [{}] ", millis, method);

        Thread.sleep(millis);
    }
}
