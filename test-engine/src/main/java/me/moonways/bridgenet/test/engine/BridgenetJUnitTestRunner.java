package me.moonways.bridgenet.test.engine;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactory;
import me.moonways.bridgenet.api.inject.bean.factory.ConstructorFactory;
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

@Log4j2
public class BridgenetJUnitTestRunner extends BlockJUnit4ClassRunner {

    private static final TestBridgenetBootstrap BOOTSTRAP = new TestBridgenetBootstrap();
    private static final BeanFactory BEAN_FACTORY = new ConstructorFactory();

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
            Object testClassInstance = BEAN_FACTORY.create(testClass);
            TestUnit testUnit = new TestUnit(notifier, testClassInstance);

            BOOTSTRAP.init(testClassInstance);

            for (TestRunnableStep step : testRunnableStepList) {
                log.info("§6TestEngine has processing step - {}", step.getClass().getSimpleName());

                step.process(BOOTSTRAP, testUnit);
            }
        }
        catch (Exception exception) {
            BOOTSTRAP.throwException(exception);
        }

        notifier.fireTestFinished(getDescription());
        BOOTSTRAP.shutdown();
    }
}
