package me.moonways.bridgenet.test.engine;

import me.moonways.bridgenet.api.inject.factory.ObjectFactory;
import me.moonways.bridgenet.api.inject.factory.UnsafeObjectFactory;
import me.moonways.bridgenet.test.engine.unit.TestRunnableStep;
import me.moonways.bridgenet.test.engine.unit.TestUnit;
import me.moonways.bridgenet.test.engine.unit.step.TestCreateStep;
import me.moonways.bridgenet.test.engine.unit.step.TestEmulateRunningStep;
import me.moonways.bridgenet.test.engine.unit.step.TestFinishStep;
import me.moonways.bridgenet.test.engine.unit.step.TestInvocationStep;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.Arrays;
import java.util.List;

public class BridgenetJUnitTestRunner extends BlockJUnit4ClassRunner {

    private static final BridgenetBootstrapInitializer BOOTSTRAP_INITIALIZER = new BridgenetBootstrapInitializer();
    private static final ObjectFactory OBJECT_FACTORY = new UnsafeObjectFactory();

    private final Class<?> testClass;

    private final List<TestRunnableStep> testRunnableStepList = Arrays.asList(
            new TestEmulateRunningStep(),
            new TestCreateStep(),
            new TestInvocationStep(),
            new TestFinishStep()
    );

    public BridgenetJUnitTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        this.testClass = testClass;
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            Object testClassInstance = OBJECT_FACTORY.create(testClass);
            TestUnit testUnit = new TestUnit(notifier, testClassInstance);

            BOOTSTRAP_INITIALIZER.init(testClassInstance);

            for (TestRunnableStep step : testRunnableStepList) {
                step.process(BOOTSTRAP_INITIALIZER, testUnit);
            }
        }
        catch (Exception exception) {
            BOOTSTRAP_INITIALIZER.throwException(exception);
        }
        finally {
            notifier.fireTestFinished(getDescription());
            BOOTSTRAP_INITIALIZER.shutdown();
        }
    }
}
