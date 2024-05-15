package me.moonways.bridgenet.test.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.util.reflection.ReflectionUtils;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.persistance.PersistenceAcceptType;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import me.moonways.bridgenet.test.engine.persistance.TestSleeping;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.util.Optional;

@Getter
@ToString
@RequiredArgsConstructor
public class TestingElement {

    private final FrameworkMethod frameworkMethod;

    public String getName() {
        return frameworkMethod.getName();
    }

    public final Description createJunitDescription(TestClass testClass) {
        return Description.createTestDescription(testClass.getJavaClass(),
                frameworkMethod.getName());
    }

    public void execute(TestFlowContext context) {
        TestingObject testingObject = context.getTestingObject();
        ReflectionUtils.callMethod(testingObject.getInstance(),
                frameworkMethod.getName());
    }

    public final int getElementOrder() {
        return Optional.ofNullable(frameworkMethod.getAnnotation(TestOrdered.class))
                .map(TestOrdered::value).orElse(0);
    }

    public final long getBeforeSleepingDurationMs() {
        return Optional.ofNullable(frameworkMethod.getAnnotation(TestSleeping.class))
                .filter(testSleeping -> testSleeping.acceptType() != PersistenceAcceptType.POST_EXECUTION)
                .map(TestSleeping::value).orElse(0);
    }

    public final long getPostSleepingDurationMs() {
        return Optional.ofNullable(frameworkMethod.getAnnotation(TestSleeping.class))
                .filter(testSleeping -> testSleeping.acceptType() == PersistenceAcceptType.POST_EXECUTION)
                .map(TestSleeping::value).orElse(0);
    }
}
