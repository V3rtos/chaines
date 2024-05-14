package me.moonways.bridgenet.test.engine;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactory;
import me.moonways.bridgenet.api.inject.bean.factory.type.ConstructorFactory;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.test.engine.unit.ExternalUnit;
import me.moonways.bridgenet.test.engine.unit.TestClassUnit;
import me.moonways.bridgenet.test.engine.unit.TestRunnableStep;
import me.moonways.bridgenet.test.engine.unit.step.TestCreateStep;
import me.moonways.bridgenet.test.engine.unit.step.TestEmulateRunningStep;
import me.moonways.bridgenet.test.engine.unit.step.TestFinishStep;
import me.moonways.bridgenet.test.engine.unit.step.TestInvocationStep;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
            BeansService beansService = BOOTSTRAP.init(testClassInstance);

            TestClassUnit unit = new TestClassUnit(notifier, testClassInstance);

            Set<ExternalUnit> delegateExternalsUnits = unit.getDelegateExternalsUnits();
            for (ExternalUnit externalUnit : delegateExternalsUnits) {
                log.debug("§6Injecting external Test unit: {}", externalUnit.getName());

                externalUnit.inject(notifier, beansService, this);
            }

            processing(unit);
        }
        catch (Exception exception) {
            BOOTSTRAP.throwException(exception);
        }

        notifier.fireTestFinished(getDescription());
        BOOTSTRAP.shutdown();
    }

    public void processing(TestClassUnit unit) {
        for (TestRunnableStep step : testRunnableStepList) {
            log.info("§6Engine was processing step {} for {}", step.getClass().getSimpleName(), unit.getName());
            try {
                step.process(BOOTSTRAP, unit);
            } catch (Exception exception) {
                throw new TestEngineException(exception);
            }
        }
    }
}
